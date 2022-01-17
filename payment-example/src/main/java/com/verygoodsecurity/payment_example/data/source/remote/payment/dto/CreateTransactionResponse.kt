package com.verygoodsecurity.payment_example.data.source.remote.payment.dto

import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.payment_example.data.entity.Transaction

data class CreateTransactionResponse constructor(
    @SerializedName("data") val transaction: Transaction
)