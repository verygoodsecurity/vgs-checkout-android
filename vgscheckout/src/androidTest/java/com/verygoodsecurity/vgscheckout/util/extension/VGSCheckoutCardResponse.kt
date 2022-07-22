package com.verygoodsecurity.vgscheckout.util.extension

import com.google.gson.JsonParser
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

internal fun VGSCheckoutCardResponse.getId(): String {
    return JsonParser
        .parseString(body?.trim())
        .asJsonObject
        .run {
            takeIf { it.has("data") }?.run { get("data").asJsonObject }
                ?.takeIf { it.has("id") }?.run { get("id").asString }
                ?: ""
        }
}