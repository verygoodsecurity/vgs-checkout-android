package com.verygoodsecurity.democheckout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonParser
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult.*

class MainActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val applicationClient: HttpClient by lazy {
        HttpClient()
    }

    private lateinit var checkout: VGSCheckout

    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            if(code in 200..299) {
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

    //region Checkout config
    private fun getCheckoutConfig() = VGSCheckoutMultiplexingConfiguration(
        vaultID = VAULT_ID,
        token = accessToken,
    )

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        showTransactionDialog(result)
    }

    private fun showTransactionDialog(result: VGSCheckoutResult) {
        TransactionDialogFragment().apply {
            arguments = when (result) {
                is Success -> Bundle().apply {
                    result.code?.let { putInt(TransactionDialogFragment.CODE, it) }
                    putString(TransactionDialogFragment.TNT, VAULT_ID)
                    putString(TransactionDialogFragment.BODY, result.body)
                }
                is Failed -> Bundle().apply {
                    result.code?.let { putInt(TransactionDialogFragment.CODE, it) }
                    putString(TransactionDialogFragment.TNT, VAULT_ID)
                    putString(TransactionDialogFragment.BODY, result.body)
                }
                is Canceled -> Bundle()
            }
        }.show(supportFragmentManager, TransactionDialogFragment::class.java.canonicalName)
    }

    companion object {

        //todo replace it to the local.properties file.
        val VAULT_ID = "tntshmljla7"
    }
}