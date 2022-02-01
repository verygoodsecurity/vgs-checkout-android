package com.verygoodsecurity.payments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.payments.data.Result
import com.verygoodsecurity.payments.data.repository.auth.AccessTokenRepository
import com.verygoodsecurity.payments.data.repository.auth.DefaultAccessTokenRepository
import com.verygoodsecurity.payments.data.repository.order.DefaultOrderRepository
import com.verygoodsecurity.payments.data.repository.order.OrderRepository
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutTransactionResponse
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val tokenRepository: AccessTokenRepository = DefaultAccessTokenRepository()
    private val orderRepository: OrderRepository = DefaultOrderRepository()

    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            handlePayClicked()
        }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        val resultData: VGSCheckoutResultBundle? = when (result) {
            is VGSCheckoutResult.Success -> result.data
            is VGSCheckoutResult.Failed -> result.data
            else -> null
        }
        val addCardResponse =
            resultData?.getParcelable<VGSCheckoutAddCardResponse>(VGSCheckoutResultBundle.ADD_CARD_RESPONSE)
        val transactionResponse =
            resultData?.getParcelable<VGSCheckoutTransactionResponse>(VGSCheckoutResultBundle.TRANSACTION_RESPONSE)
        val shouldSaveCard = resultData?.getBoolean(VGSCheckoutResultBundle.SHOULD_SAVE_CARD)
        Log.d(
            "VGSCheckout", """
            ${result::class.java.simpleName}
            Add card response = $addCardResponse
            Transaction response = $transactionResponse
            Should save card = $shouldSaveCard
        """.trimIndent()
        )
    }

    private fun handlePayClicked() {
        lifecycleScope.launch {
            val tokenResult = tokenRepository.get()
            if (tokenResult is Result.Success) {
                val orderResult = orderRepository.create()
                if (orderResult is Result.Success) {
                    startCheckout(tokenResult.data, orderResult.data.id)
                } else {
                    Log.d("VGSCheckout", "Can't create order! ${(tokenResult as Result.Error).msg}")
                }
            } else {
                Log.d(
                    "VGSCheckout",
                    "Can't fetch access token! ${(tokenResult as Result.Error).msg}"
                )
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun startCheckout(accessToken: String, orderId: String) {
//        VGSCheckoutPaymentConfig.create(
//            accessToken,
//            orderId,
//            BuildConfig.VAULT_ID,
//            object : VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig> {
//
//                override fun onSuccess(config: VGSCheckoutPaymentConfig) {
//                    checkout.present(config)
//                }
//
//                override fun onFailure(exception: VGSCheckoutException) {
//                    Log.d("VGSCheckout", "Can't create checkout config! ${exception.message}")
//                }
//            }
//        )
    }
}