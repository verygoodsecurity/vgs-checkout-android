package com.verygoodsecurity.vgscheckout.util.extension

import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import java.io.StringReader

internal fun VGSCheckoutCardResponse.getId(): String {
    return JsonParser
        .parseReader(JsonReader(StringReader(body?.trim())).also { it.isLenient = true })
        .asJsonObject
        .run {
            takeIf { it.has("data") }?.run { get("data").asJsonObject }
                ?.takeIf { it.has("id") }?.run { get("id").asString }
                ?: ""
        }
}