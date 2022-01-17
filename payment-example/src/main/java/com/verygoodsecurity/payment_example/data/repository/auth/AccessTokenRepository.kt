package com.verygoodsecurity.payment_example.data.repository.auth

import com.verygoodsecurity.payment_example.data.Result

interface AccessTokenRepository {

    suspend fun get(): Result<String>
}