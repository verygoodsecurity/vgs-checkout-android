package com.verygoodsecurity.vgscheckout.util.gpay

import android.content.Context
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants

class PaymentUtils constructor(context: Context) {

    private val paymentClient = Wallet.getPaymentsClient(
        context,
        Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
    )

    // TODO: Add payload

    fun isReadyToPay(onResult: (Boolean) -> Unit) {
        paymentClient.isReadyToPay(IsReadyToPayRequest.fromJson((1 + 1).toString().also {
            Log.d("Test", it)
        }))
            .addOnCompleteListener {
                try {
                    it.getResult(ApiException::class.java)?.let(onResult)
                } catch (e: ApiException) {
                    onResult.invoke(false)
                }
            }
    }
}