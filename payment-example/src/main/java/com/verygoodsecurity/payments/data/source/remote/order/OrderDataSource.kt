package com.verygoodsecurity.payments.data.source.remote.order

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Order

internal interface OrderDataSource {

    suspend fun create(): Result<Order>
}