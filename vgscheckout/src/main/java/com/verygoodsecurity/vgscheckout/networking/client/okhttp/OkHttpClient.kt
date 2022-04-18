package com.verygoodsecurity.vgscheckout.networking.client.okhttp

import com.verygoodsecurity.vgscheckout.networking.client.okhttp.interceptor.CustomHostnameInterceptor
import com.verygoodsecurity.vgscheckout.networking.client.okhttp.interceptor.LoggingInterceptor
import com.verygoodsecurity.vgscheckout.networking.toHost
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.networking.client.HttpBodyFormat
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.isURLValid
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

    override fun enqueue(request: HttpRequest, callback: ((HttpResponse) -> Unit)?) {
        if (!request.url.isURLValid()) {
            callback?.invoke(HttpResponse.create(VGSError.URL_NOT_VALID))
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
                            callback?.invoke(HttpResponse.create(VGSError.TIME_OUT))
                        } else {
                            callback?.invoke(HttpResponse(message = e.message))
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        callback?.invoke(
                            HttpResponse(
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
            callback?.invoke(HttpResponse(message = e.message))
        }
    }

    override fun execute(request: HttpRequest): HttpResponse {
        if (!request.url.isURLValid()) {
            return HttpResponse.create(VGSError.URL_NOT_VALID)
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

            HttpResponse(
                response.isSuccessful,
                response.code,
                response.body?.string(),
                response.message,
                latency = response.latency()
            )
        } catch (e: InterruptedIOException) {
            HttpResponse.create(VGSError.TIME_OUT)
        } catch (e: TimeoutException) {
            HttpResponse.create(VGSError.TIME_OUT)
        } catch (e: IOException) {
            HttpResponse(message = e.message)
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
        val mediaType = contentType.value.toMediaTypeOrNull()
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