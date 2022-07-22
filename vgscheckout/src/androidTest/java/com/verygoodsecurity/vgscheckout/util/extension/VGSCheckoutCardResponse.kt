package com.verygoodsecurity.vgscheckout.util.extension

import com.google.gson.Gson
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

internal fun VGSCheckoutCardResponse.getId(): String {
    return Gson().fromJson(body, CardResponse::class.java).data.id
}

private data class CardResponse(
    val data: CardDataResponse
)

private data class CardDataResponse(
    val id: String
)