package com.verygoodsecurity.payment_example.data.repository.transaction

import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.entity.Transaction

interface TransactionRepository {

    suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction>
}