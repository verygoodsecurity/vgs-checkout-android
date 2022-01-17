package com.verygoodsecurity.payment_example.data.repository.order

import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.entity.Order
import com.verygoodsecurity.payment_example.data.repository.order.OrderRepository
import com.verygoodsecurity.payment_example.data.source.remote.order.DefaultOrderDataSource
import com.verygoodsecurity.payment_example.data.source.remote.order.OrderDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultOrderRepository : OrderRepository {

    private val source: OrderDataSource = DefaultOrderDataSource()

    override suspend fun create(): Result<Order> = withContext(Dispatchers.IO) { source.create() }
}