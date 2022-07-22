package com.verygoodsecurity.vgscheckout.util.extension

import com.google.gson.Gson
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal fun VGSCheckoutCardResponse.getId(): String {
    VGSCheckoutLogger.warn("VGSCheckout", "body:$body")
    return Gson().fromJson(
        body?.replace("^\"|\"$", ""),
        CardResponse::class.java
    ).data.id
}

private data class CardResponse(
    val data: CardDataResponse
)

private data class CardDataResponse(
    val id: String
)