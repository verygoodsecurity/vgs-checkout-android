package com.verygoodsecurity.vgscheckout.util.extension

import com.google.gson.JsonParser
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal fun VGSCheckoutCardResponse.getId(): String {
    VGSCheckoutLogger.warn("VGSCheckout", "body:$body")
    return JsonParser
        .parseString(body)
        .asJsonObject
        .run {
            takeIf { it.has("data") }?.run { get("data").asJsonObject }
                ?.takeIf { it.has("id") }?.run { get("id").asString }
                ?: ""
        }
}