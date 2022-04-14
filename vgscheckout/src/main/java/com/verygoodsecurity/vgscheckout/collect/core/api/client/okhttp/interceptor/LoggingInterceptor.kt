package com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp.interceptor

import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.*

private const val TAG = "VGSCheckout"

internal class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestId = UUID.randomUUID().toString()
        return chain.proceed(chain.request().also {
            logRequest(requestId, it)
        }).also {
            logResponse(requestId, it.request.url.toString(), it)
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

    private fun logRequest(requestId: String, request: Request) {
        VGSCheckoutLogger.debug(
            TAG,
            """
            --> Request
            --> id: $requestId
            --> url: ${request.url}
            --> method: ${request.method}
            --> headers: ${request.headers.toMap()}
            --> payload: ${getBody(request.body)}
        """
        )
    }

    private fun logResponse(requestId: String, requestUrl: String, response: Response) {
        VGSCheckoutLogger.debug(
            TAG,
            """
            <-- Response
            <-- id: $requestId
            <-- url: $requestUrl
            <-- code: ${response.code}
            <-- message: ${response.message}
            <-- headers: ${response.headers.toMap()}
        """
        )
    }
}