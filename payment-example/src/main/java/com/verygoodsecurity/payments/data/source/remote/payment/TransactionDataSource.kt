package com.verygoodsecurity.payments.data.source.remote.payment

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Transaction

internal interface TransactionDataSource {

    suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction>
}