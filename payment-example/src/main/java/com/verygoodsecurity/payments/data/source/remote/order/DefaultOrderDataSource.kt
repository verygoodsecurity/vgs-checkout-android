package com.verygoodsecurity.payments.data.source.remote.order

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Order
import com.verygoodsecurity.payments.data.source.remote.BASE_URL
import com.verygoodsecurity.payments.data.source.remote.BaseRemoteDataSource
import com.verygoodsecurity.payments.data.source.remote.RetrofitHelper
import com.verygoodsecurity.payments.data.source.remote.order.dto.CreateOrderResponse
import retrofit2.Response
import retrofit2.http.POST

internal class DefaultOrderDataSource : BaseRemoteDataSource(), OrderDataSource {

    private val client = RetrofitHelper.build(BASE_URL).create(API::class.java)

    override suspend fun create(): Result<Order> = safeCall {
        val response = client.create()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Result.Success(body.order)
        } else {
            Result.Error(response.code(), response.errorBody()?.toString())
        }
    }

    interface API {

        @POST("orders")
        suspend fun create(): Response<CreateOrderResponse>
    }
}