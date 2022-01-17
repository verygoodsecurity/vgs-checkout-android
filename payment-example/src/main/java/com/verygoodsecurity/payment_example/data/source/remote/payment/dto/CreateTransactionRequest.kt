package com.verygoodsecurity.payment_example.data.source.remote.payment.dto

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest constructor(
    @SerializedName("fi_id") val finId: String,
    @SerializedName("tnt") val vaultId: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("currency") val currency: String,
)