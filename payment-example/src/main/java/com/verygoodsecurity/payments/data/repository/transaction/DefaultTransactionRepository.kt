package com.verygoodsecurity.payments.data.repository.transaction

import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.entity.Transaction
import com.verygoodsecurity.payments.data.source.remote.payment.DefaultTransactionDataSource
import com.verygoodsecurity.payments.data.source.remote.payment.TransactionDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultTransactionRepository : TransactionRepository {

    private val source: TransactionDataSource = DefaultTransactionDataSource()

    override suspend fun create(
        finId: String,
        vaultId: String,
        amount: Long,
        currency: String
    ): Result<Transaction> = withContext(Dispatchers.IO) {
        source.create(finId, vaultId, amount, currency)
    }
}