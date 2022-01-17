package com.verygoodsecurity.payments.data.source.remote.payment

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Transaction
import com.verygoodsecurity.payments.data.source.remote.BASE_URL
import com.verygoodsecurity.payments.data.source.remote.BaseRemoteDataSource
import com.verygoodsecurity.payments.data.source.remote.RetrofitHelper
import com.verygoodsecurity.payments.data.source.remote.payment.dto.CreateTransactionRequest
import com.verygoodsecurity.payments.data.source.remote.payment.dto.CreateTransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal class DefaultTransactionDataSource : BaseRemoteDataSource(), TransactionDataSource {

    private val client = RetrofitHelper.build(BASE_URL).create(API::class.java)

    override suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction> = safeCall {
        val response = client.create(CreateTransactionRequest(finId, vaultId, amount, currency))
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Result.Success(body.transaction)
        } else {
            Result.Error(response.code(), response.errorBody()?.toString())
        }
    }

    interface API {

        @POST("transfers")
        suspend fun create(@Body payload: CreateTransactionRequest): Response<CreateTransactionResponse>
    }
}