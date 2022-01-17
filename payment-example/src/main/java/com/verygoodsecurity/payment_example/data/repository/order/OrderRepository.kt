package com.verygoodsecurity.payment_example.data.repository.order

import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.entity.Order

interface OrderRepository {

    suspend fun create(): Result<Order>
}