package com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp

import com.verygoodsecurity.vgscheckout.collect.core.HttpMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.HttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.client.HttpClient
import com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp.interceptor.CustomHostnameInterceptor
import com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp.interceptor.LoggingInterceptor
import com.verygoodsecurity.vgscheckout.collect.core.api.isURLValid
import com.verygoodsecurity.vgscheckout.collect.core.api.toContentType
import com.verygoodsecurity.vgscheckout.collect.core.api.toHost
import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException
import java.io.InterruptedIOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class OkHttpClient(
    printLogs: Boolean = true,
    executor: ExecutorService = Executors.newSingleThreadExecutor()
) : HttpClient {

    private val hostInterceptor: CustomHostnameInterceptor = CustomHostnameInterceptor()

    private val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(hostInterceptor)
            .dispatcher(Dispatcher(executor)).also {
                if (printLogs) it.addInterceptor(LoggingInterceptor())
            }
            .build()
    }

    override fun setHost(url: String?) {
        hostInterceptor.host = url?.toHost()
    }

    override fun enqueue(request: HttpRequest, callback: ((NetworkResponse) -> Unit)?) {
        if (!request.url.isURLValid()) {
            callback?.invoke(NetworkResponse.create(VGSError.URL_NOT_VALID))
            return
        }

        val okHttpRequest = buildRequest(
            request.url,
            request.method,
            request.headers,
            request.payload,
            request.format
        )

        try {
            client.newBuilder()
                .callTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
                .readTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
                .writeTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
                .build()
                .newCall(okHttpRequest).enqueue(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        VGSCheckoutLogger.warn(this::class.java.simpleName, e)
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
            VGSCheckoutLogger.warn(this::class.java.simpleName, e)
            callback?.invoke(NetworkResponse(message = e.message))
        }
    }

    override fun execute(request: HttpRequest): NetworkResponse {
        if (!request.url.isURLValid()) {
            return NetworkResponse.create(VGSError.URL_NOT_VALID)
        }

        val okHttpRequest = buildRequest(
            request.url,
            request.method,
            request.headers,
            request.payload,
            request.format
        )

        return try {
            val response = client.newBuilder()
                .callTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
                .readTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
                .writeTimeout(request.timeoutInterval, TimeUnit.MILLISECONDS)
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
        method: HttpMethod,
        headers: Map<String, String>?,
        data: Any?,
        contentType: HttpBodyFormat = HttpBodyFormat.JSON
    ): Request {
        val mediaType = contentType.toContentType().toMediaTypeOrNull()
        val requestBody = data?.toString().toRequestBodyOrNull(mediaType, method)
        return Request.Builder()
            .url(url)
            .method(method.name, requestBody)
            .addHeaders(headers)
            .build()
    }

    private fun Response.latency() = this.receivedResponseAtMillis - this.sentRequestAtMillis

    private fun String?.toRequestBodyOrNull(mediaType: MediaType?, method: HttpMethod) =
        when (method) {
            HttpMethod.GET -> null
            else -> this?.toRequestBody(mediaType) ?: EMPTY_REQUEST
        }

    private fun Request.Builder.addHeaders(headers: Map<String, String>?): Request.Builder {
        headers?.forEach {
            this.addHeader(it.key, it.value)
        }
        return this
    }
}