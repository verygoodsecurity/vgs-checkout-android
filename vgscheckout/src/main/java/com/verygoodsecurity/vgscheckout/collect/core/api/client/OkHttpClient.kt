package com.verygoodsecurity.vgscheckout.collect.core.api.client

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.*
import com.verygoodsecurity.vgscheckout.collect.core.api.client.extension.*
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okio.Buffer
import java.io.IOException
import java.io.InterruptedIOException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class OkHttpClient(
    printLogs: Boolean = true,
    executor: ExecutorService = Executors.newSingleThreadExecutor()
) : ApiClient {

    private val hostInterceptor: HostInterceptor = HostInterceptor()

    private val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(hostInterceptor)
            .dispatcher(Dispatcher(executor)).also {
                if (printLogs) it.addInterceptor(HttpLoggingInterceptor())
            }
            .build()
    }

    override fun setHost(url: String?) {
        hostInterceptor.host = url?.toHost()
    }

    override fun enqueue(request: NetworkRequest, callback: ((NetworkResponse) -> Unit)?) {
        if (!request.url.isURLValid()) {
            callback?.invoke(NetworkResponse.create(VGSError.URL_NOT_VALID))
            return
        }

        val okHttpRequest = buildRequest(
            request.url,
            request.method,
            request.customHeader,
            request.customData,
            request.format
        )

        try {
            client.newBuilder()
                .callTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .readTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .writeTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .build()
                .newCall(okHttpRequest).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    logException(e)
                    if (call.isCanceled()) {
                        return
                    }
                    if (e is InterruptedIOException || e is TimeoutException) {
                        callback?.invoke(NetworkResponse.create(VGSError.TIME_OUT))
                    } else {
                        callback?.invoke(NetworkResponse(message = e.message))
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    callback?.invoke(
                        NetworkResponse(
                            response.isSuccessful,
                            response.code,
                            response.body?.string(),
                            response.message,
                            latency = response.latency()
                        )
                    )
                }
            })
        } catch (e: Exception) {
            logException(e)
            callback?.invoke(NetworkResponse(message = e.message))
        }
    }

    override fun execute(request: NetworkRequest): NetworkResponse {
        if (!request.url.isURLValid()) {
            return NetworkResponse.create(VGSError.URL_NOT_VALID)
        }

        val okHttpRequest = buildRequest(
            request.url,
            request.method,
            request.customHeader,
            request.customData,
            request.format
        )

        return try {
            val response = client.newBuilder()
                .callTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .readTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .writeTimeout(request.requestTimeoutInterval, TimeUnit.MILLISECONDS)
                .build()
                .newCall(okHttpRequest).execute()

            NetworkResponse(
                response.isSuccessful,
                response.code,
                response.body?.string(),
                response.message,
                latency = response.latency()
            )
        } catch (e: InterruptedIOException) {
            NetworkResponse.create(VGSError.TIME_OUT)
        } catch (e: TimeoutException) {
            NetworkResponse.create(VGSError.TIME_OUT)
        } catch (e: IOException) {
            NetworkResponse(message = e.message)
        }
    }

    override fun cancelAll() {
        client.dispatcher.cancelAll()
    }

    private fun buildRequest(
        url: String,
        method: HTTPMethod,
        headers: Map<String, String>?,
        data: Any?,
        contentType: VGSHttpBodyFormat = VGSHttpBodyFormat.JSON
    ): Request {
        val mediaType = contentType.toContentType().toMediaTypeOrNull()
        val requestBody = data?.toString().toRequestBodyOrNull(mediaType, method)
        return Request.Builder()
            .url(url)
            .method(method.name, requestBody)
            .addHeaders(headers)
            .build()
    }

    private fun Request.Builder.addHeaders(headers: Map<String, String>?): Request.Builder {
        headers?.forEach {
            this.addHeader(it.key, it.value)
        }
        return this
    }

    private class HostInterceptor : Interceptor {
        var host: String? = null
        override fun intercept(chain: Interceptor.Chain): Response {
            val r = with(chain.request()) {
                if (!host.isNullOrBlank() && host != url.host) {
                    val newUrl = chain.request().url.newBuilder()
                        .scheme(url.scheme)
                        .host(host!!)
                        .build()

                    chain.request().newBuilder()
                        .url(newUrl)
                        .build()
                } else {
                    this
                }
            }

            return chain.proceed(r)
        }
    }

    private class HttpLoggingInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val requestId = UUID.randomUUID().toString()
            return chain.proceed(chain.request().also {
                it.logRequest(
                    requestId,
                    it.url.toString(),
                    it.method,
                    it.headers.toMap(),
                    getBody(it.body),
                    it::class.java.simpleName
                )
            }).also {
                logResponse(
                    requestId,
                    it.request.url.toString(),
                    it.code,
                    it.message,
                    it.headers.toMap(),
                    it::class.java.simpleName
                )
            }
        }

        private fun getBody(request: RequestBody?): String {
            return try {
                val buffer = Buffer()
                request?.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                ""
            }
        }
    }
}