package com.verygoodsecurity.payments.data.source.remote.order.dto

import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.payments.data.entity.Order

data class CreateOrderResponse constructor(
    @SerializedName("data") val order: Order
)