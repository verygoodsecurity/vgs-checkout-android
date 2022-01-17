package com.verygoodsecurity.payments.data.source.remote.auth

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.source.remote.BASE_URL
import com.verygoodsecurity.payments.data.source.remote.BaseRemoteDataSource
import com.verygoodsecurity.payments.data.source.remote.RetrofitHelper
import com.verygoodsecurity.payments.data.source.remote.auth.dto.GetAccessTokenResponse
import retrofit2.Response
import retrofit2.http.POST

internal class DefaultAccessTokenDataSource : BaseRemoteDataSource(), AccessTokenDataSource {

    private val client = RetrofitHelper.build(BASE_URL).create(API::class.java)

    override suspend fun get(): Result<String> = safeCall {
        val response = client.get()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Result.Success(body.token)
        } else {
            Result.Error(response.code(), response.errorBody()?.toString())
        }
    }

    interface API {

        @POST("get-auth-token")
        suspend fun get(): Response<GetAccessTokenResponse>
    }
}