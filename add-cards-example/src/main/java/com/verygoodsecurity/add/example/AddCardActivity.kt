package com.verygoodsecurity.add.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.R
import com.verygoodsecurity.add.example.network.HttpClient
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

class AddCardActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String by lazy { getString(R.string.storage_id) }

    private val accessClient: HttpClient by lazy { HttpClient() }

    private lateinit var checkout: VGSCheckout

    private var config: VGSCheckoutAddCardConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshToken()

        checkout = VGSCheckout(this, this)

        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
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