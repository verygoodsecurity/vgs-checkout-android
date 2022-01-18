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
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
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
        when (result) {
            is VGSCheckoutResult.Success -> {
                Log.d("VGSCheckout", "Success!")
                Log.d("VGSCheckout", "Add card response = ${result.data.getAddCardResponse()}")
                Log.d(
                    "VGSCheckout",
                    "Transaction response = ${result.data.getTransactionResponse()}"
                )
                Log.d(
                    "VGSCheckout",
                    "Should save card = ${result.data.shouldSaveCard()}"
                )
            }
            is VGSCheckoutResult.Failed -> {
                Log.d("VGSCheckout", "Failed!")
                Log.d("VGSCheckout", "Add card response = ${result.data.getAddCardResponse()}")
                Log.d(
                    "VGSCheckout",
                    "Transaction response = ${result.data.getTransactionResponse()}"
                )
            }
            is VGSCheckoutResult.Canceled -> Log.d("VGSCheckout", "Canceled!")
        }
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
        VGSCheckoutPaymentConfig.create(
            accessToken,
            orderId,
            BuildConfig.VAULT_ID,
            object : VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig> {

                override fun onSuccess(config: VGSCheckoutPaymentConfig) {
                    checkout.present(config)
                }

                override fun onFailure(exception: VGSCheckoutException) {
                    Log.d("VGSCheckout", "Can't create checkout config! ${exception.message}")
                }
            }
        )
    }
}