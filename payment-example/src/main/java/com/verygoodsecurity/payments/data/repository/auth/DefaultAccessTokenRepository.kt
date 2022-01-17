package com.verygoodsecurity.payments.data.repository.auth

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.source.remote.auth.AccessTokenDataSource
import com.verygoodsecurity.payments.data.source.remote.auth.DefaultAccessTokenDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultAccessTokenRepository : AccessTokenRepository {

    private val source: AccessTokenDataSource = DefaultAccessTokenDataSource()

    override suspend fun get(): Result<String> = withContext(Dispatchers.IO) { source.get() }
}