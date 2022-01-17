package com.verygoodsecurity.payment_example.data.source.remote.order

import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.entity.Order

internal interface OrderDataSource {

    suspend fun create(): Result<Order>
}