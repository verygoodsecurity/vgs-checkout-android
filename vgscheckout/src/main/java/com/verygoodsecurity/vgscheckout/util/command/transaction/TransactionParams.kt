package com.verygoodsecurity.vgscheckout.util.command.transaction

internal class TransactionParams constructor(
    val finId: String,
    val vaultId: String,
    val amount: Long,
    val currency: String
)