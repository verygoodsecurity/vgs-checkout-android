package com.verygoodsecurity.payment_example.data.entity

import com.google.gson.annotations.SerializedName

data class Transaction constructor(
    @SerializedName("id") val id: String,
    @SerializedName("source") val finId: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("amount_captured") val amountCaptured: Long,
    @SerializedName("amount_reversed") val amountReversed: Long,
    @SerializedName("captures") val captures: List<Any>,
    @SerializedName("currency") val currency: String,
    @SerializedName("destination") val destination: String?,
    @SerializedName("fee") val fee: Int,
    @SerializedName("gateway") val gateway: String,
    @SerializedName("gateway_response") val gatewayResponse: GetawayResponse,
    @SerializedName("state") val state: String,
    @SerializedName("type") val type: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
) {

    data class GetawayResponse constructor(
        @SerializedName("error_code") val errorCode: String,
        @SerializedName("id") val id: String,
        @SerializedName("message") val message: String,
        @SerializedName("raw_response") val rawResponse: String,
        @SerializedName("state") val state: String,
    )
}