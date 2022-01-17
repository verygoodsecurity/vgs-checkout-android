package com.verygoodsecurity.payments.data.repository.auth

import com.verygoodsecurity.payments.data.Result

interface AccessTokenRepository {

    suspend fun get(): Result<String>
}