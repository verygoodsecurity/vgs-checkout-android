package com.verygoodsecurity.payment_example.data.source.remote.payment

import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.entity.Transaction

internal interface TransactionDataSource {

    suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction>
}