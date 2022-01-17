package com.verygoodsecurity.payments.data.source.remote.payment.dto

import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.payments.data.entity.Transaction

data class CreateTransactionResponse constructor(
    @SerializedName("data") val transaction: Transaction
)