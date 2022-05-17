package com.verygoodsecurity.vgscheckout.demo.add

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.demo.BuildConfig.STORAGE_ID
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.demo.add.network.HttpClient
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

class PaymentCheckoutActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String = STORAGE_ID

    private val accessClient: HttpClient by lazy { HttpClient() }

    private lateinit var checkout: VGSCheckout

    private var config: VGSCheckoutAddCardConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_checkout)

        refreshToken()

        checkout = VGSCheckout(this, this)

        findViewById<MaterialButton>(R.id.mbPresent).setOnClickListener {
            config?.let { checkout.present(it) }
        }
    }


    override fun onCheckoutResult(result: VGSCheckoutResult) {
        if (result is VGSCheckoutResult.Canceled) {
            refreshToken()
        } else {
            printResults(result)
        }
    }

    private fun printResults(result: VGSCheckoutResult) {
        when (result) {
            is VGSCheckoutResult.Success ->
                result.data.getParcelable<VGSCheckoutCardResponse>(
                    VGSCheckoutResultBundle.Keys.ADD_CARD_RESPONSE
                ).let { Log.e(TAG, "Success \n ${it?.body}") }
            is VGSCheckoutResult.Failed ->
                result.data.getParcelable<VGSCheckoutCardResponse>(
                    VGSCheckoutResultBundle.Keys.ADD_CARD_RESPONSE
                ).let { Log.e(TAG, "Failed \n ${it?.body}") }
            is VGSCheckoutResult.Canceled -> Log.e(TAG, "Canceled")
        }
    }

    private fun refreshToken() {
        accessClient.getAccessToken { token ->
            config = token?.run {
                VGSCheckoutAddCardConfig(
                    token,
                    vaultId
                )
            }
        }
    }

    companion object {
        private const val TAG = "VGS Checkout Result"
    }
}