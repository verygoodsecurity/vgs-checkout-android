package com.verygoodsecurity.payments.data.entity

import com.google.gson.annotations.SerializedName

data class Order constructor(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("currency") val currency: String
)