package com.verygoodsecurity.payments.data.source.remote.auth

import com.verygoodsecurity.payments.data.Result

internal interface AccessTokenDataSource {

    suspend fun get(): Result<String>
}