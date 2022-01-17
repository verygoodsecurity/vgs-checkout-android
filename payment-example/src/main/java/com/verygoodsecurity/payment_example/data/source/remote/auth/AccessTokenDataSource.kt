package com.verygoodsecurity.payment_example.data.source.remote.auth

import com.verygoodsecurity.payment_example.data.Result

internal interface AccessTokenDataSource {

    suspend fun get(): Result<String>
}