package com.verygoodsecurity.payments.data.repository.transaction

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Transaction

interface TransactionRepository {

    suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction>
}