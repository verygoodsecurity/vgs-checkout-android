package com.verygoodsecurity.vgscheckout.networking.client.okhttp

import com.verygoodsecurity.vgscheckout.exception.internal.InvalidUrlException
import com.verygoodsecurity.vgscheckout.exception.internal.TimeoutNetworkingException
import com.verygoodsecurity.vgscheckout.exception.internal.UnexpectedNetworkingException
import com.verygoodsecurity.vgscheckout.networking.client.*
import com.verygoodsecurity.vgscheckout.networking.client.okhttp.interceptor.CustomHostnameInterceptor
import com.verygoodsecurity.vgscheckout.networking.client.okhttp.interceptor.LoggingInterceptor
import com.verygoodsecurity.vgscheckout.networking.toHost
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
        try {
            val okHttpRequest = buildRequest(
                request.url,
                request.method,
                request.headers,
                request.payload,
                request.format
            )

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
                        callback?.invoke(createResponse(e))
                    }

                    override fun onResponse(call: Call, response: Response) {
                        callback?.invoke(
                            HttpResponse(
                                response.isSuccessful,
                                response.code,
                                response.body?.string(),
                                response.message,
                                response.latency()
                            )
                        )
                    }
                })
        } catch (e: Exception) {
            callback?.invoke(createResponse(e))
        }
    }

    override fun execute(request: HttpRequest): HttpResponse {
        return try {
            val okHttpRequest = buildRequest(
                request.url,
                request.method,
                request.headers,
                request.payload,
                request.format
            )

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
                response.latency()
            )
        } catch (e: Exception) {
            createResponse(e)
        }
    }

    override fun cancelAll() {
        client.dispatcher.cancelAll()
    }

    @Throws(IllegalArgumentException::class)
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

    private fun createResponse(e: Exception): HttpResponse = HttpResponse.create(
        when (e) {
            is IllegalArgumentException -> InvalidUrlException()
            is InterruptedIOException -> TimeoutNetworkingException()
            else -> UnexpectedNetworkingException(e)
        }
    )
}

private fun Response.latency() = this.receivedResponseAtMillis - this.sentRequestAtMillis

private fun String?.toRequestBodyOrNull(
    mediaType: MediaType?,
    method: HttpMethod
) = when (method) {
    HttpMethod.GET -> null
    else -> this?.toRequestBody(mediaType) ?: EMPTY_REQUEST
}

private fun Request.Builder.addHeaders(headers: Map<String, String>?): Request.Builder {
    headers?.forEach {
        this.addHeader(it.key, it.value)
    }
    return this
}