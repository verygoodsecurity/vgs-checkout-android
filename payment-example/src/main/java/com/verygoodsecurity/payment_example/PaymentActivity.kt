package com.verygoodsecurity.payment_example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import kotlinx.coroutines.launch
import com.verygoodsecurity.payment_example.data.Result
import com.verygoodsecurity.payment_example.data.repository.auth.AccessTokenRepository
import com.verygoodsecurity.payment_example.data.repository.auth.DefaultAccessTokenRepository
import com.verygoodsecurity.payment_example.data.repository.order.DefaultOrderRepository
import com.verygoodsecurity.payment_example.data.repository.order.OrderRepository

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
        showShort(
            when (result) {
                is VGSCheckoutResult.Success -> "Checkout success!"
                is VGSCheckoutResult.Failed -> "Checkout failed, reason = ${result.message}"
                is VGSCheckoutResult.Canceled -> "Checkout canceled!"
            }
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
                    showShort("Can't create order! ${(tokenResult as Result.Error).msg}")
                }
            } else {
                showShort("Can't fetch access token! ${(tokenResult as Result.Error).msg}")
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
                    showShort("Can't create checkout config! ${exception.message}")
                }
            }
        )
    }

    private fun showShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}