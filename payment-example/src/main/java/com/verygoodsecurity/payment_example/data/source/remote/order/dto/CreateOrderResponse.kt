package com.verygoodsecurity.payment_example.data.source.remote.order.dto

import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.payment_example.data.entity.Order

data class CreateOrderResponse constructor(
    @SerializedName("data") val order: Order
)