package com.verygoodsecurity.payments.data.repository.order

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Order
import com.verygoodsecurity.payments.data.source.remote.order.DefaultOrderDataSource
import com.verygoodsecurity.payments.data.source.remote.order.OrderDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultOrderRepository : OrderRepository {

    private val source: OrderDataSource = DefaultOrderDataSource()

    override suspend fun create(): Result<Order> = withContext(Dispatchers.IO) { source.create() }
}