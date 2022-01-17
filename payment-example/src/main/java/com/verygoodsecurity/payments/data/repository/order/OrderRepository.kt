package com.verygoodsecurity.payments.data.repository.order

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Order

interface OrderRepository {

    suspend fun create(): Result<Order>
}