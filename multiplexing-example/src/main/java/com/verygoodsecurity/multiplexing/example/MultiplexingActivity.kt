package com.verygoodsecurity.multiplexing.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonParser
import com.verygoodsecurity.multiplexing.R
import com.verygoodsecurity.multiplexing.example.network.HttpClient
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MultiplexingActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String by lazy { getString(R.string.vault_id) }

    private val applicationClient: HttpClient by lazy {
        HttpClient()
    }

    private lateinit var checkout: VGSCheckout

    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplexing)

        retrieveToken()
        checkout = VGSCheckout(this, this)

        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            checkout.present(getCheckoutConfig())
        }
    }

    private fun retrieveToken() {
        applicationClient.enqueue(
            "https://multiplexing-demo.apps.verygood.systems/get-auth-token",
            ""
        ) { code, body ->
            if (code in 200..299) {
                accessToken = body?.let {
                    JsonParser
                        .parseString(it)
                        .asJsonObject
                        .run {
                            takeIf { this.has("access_token") }
                                ?.run { get("access_token").asString } ?: ""
                        }
                } ?: ""

                Log.e("test", "accessToken: $accessToken")
            }
        }
    }

    private fun getCheckoutConfig() = VGSCheckoutMultiplexingConfiguration(
        vaultID = vaultId,
        token = accessToken,
    )

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        if (result !is VGSCheckoutResult.Canceled) {
            showTransactionDialog(result)
        }
    }

    private fun showTransactionDialog(result: VGSCheckoutResult) {
        TransactionDialogFragment().apply {
            arguments = when (result) {
                is VGSCheckoutResult.Success -> Bundle().apply {
                    result.code?.let { putInt(TransactionDialogFragment.CODE, it) }
                    putString(TransactionDialogFragment.TNT, vaultId)
                    putString(TransactionDialogFragment.BODY, result.body)
                }
                is VGSCheckoutResult.Failed -> Bundle().apply {
                    result.code?.let { putInt(TransactionDialogFragment.CODE, it) }
                    putString(TransactionDialogFragment.TNT, vaultId)
                    putString(TransactionDialogFragment.BODY, result.body)
                }
                is VGSCheckoutResult.Canceled -> Bundle()
            }
        }.show(supportFragmentManager, TransactionDialogFragment::class.java.canonicalName)
    }
}