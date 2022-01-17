package com.verygoodsecurity.payments.data.source.remote

import com.verygoodsecurity.payments.data.Result

internal const val BASE_URL = "https://multiplexing-demo.apps.verygood.systems/"

internal abstract class BaseRemoteDataSource {

    protected suspend fun <T : Any> safeCall(call: suspend () -> Result<T>): Result<T> {
        return try {
            call.invoke()
        } catch (e: Exception) {
            Result.Error(msg = e.message)
        }
    }
}