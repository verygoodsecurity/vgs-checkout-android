package com.verygoodsecurity.vgscheckout.collect.core.api.client.extension

import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger.warn

internal fun Any.logException(e: Exception, tag: String? = null) {
    warn(tag ?: this::class.java.simpleName, "e: ${e::class.java}, message: ${e.message}")
}

internal fun Any.logRequest(
    requestId: String,
    requestUrl: String,
    method: String,
    headers: Map<String, String>,
    payload: String?,
    tag: String? = null
) {
    VGSCheckoutLogger.debug(
        tag ?: this::class.java.simpleName,
        """
            --> Send VGSCheckout request id: $requestId
            --> Send VGSCheckout request url: $requestUrl
            --> Send VGSCheckout method: $method
            --> Send VGSCheckout request headers: $headers
            --> Send VGSCheckout request payload: $payload
        """
    )
}

internal fun Any.logResponse(
    requestId: String,
    requestUrl: String,
    responseCode: Int,
    responseMessage: String,
    headers: Map<String, String>,
    tag: String? = null
) {
    VGSCheckoutLogger.debug(
        tag ?: this::class.java.simpleName,
        """
            <-- VGSCheckout request id: $requestId
            <-- VGSCheckout request url: $requestUrl
            <-- VGSCheckout response code: $responseCode
            <-- VGSCheckout response message: $responseMessage
            <-- VGSCheckout response headers: $headers
        """
    )
}