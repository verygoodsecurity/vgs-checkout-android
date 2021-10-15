package com.verygoodsecurity.multiplexing.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonParser
import com.verygoodsecurity.multiplexing.BuildConfig.CLIENT_HOST
import com.verygoodsecurity.multiplexing.BuildConfig.GET_TOKEN_ENDPOINT
import com.verygoodsecurity.multiplexing.R
import com.verygoodsecurity.multiplexing.example.network.HttpClient
import com.verygoodsecurity.multiplexing.example.network.util.isSuccessHttpCode
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String by lazy { getString(R.string.vault_id) }

    private val applicationClient: HttpClient by lazy {
        HttpClient()
    }

    private lateinit var checkout: VGSCheckout

    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshToken { code, body ->
            if (code.isSuccessHttpCode()) accessToken = parseToken(body)
        }

        checkout = VGSCheckout(this, this)

        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            checkout.present(getCheckoutConfig())
        }
    }

    private fun refreshToken(callback: ((code: Int, body: String?) -> Unit)?) {
        applicationClient.enqueue(
            CLIENT_HOST + GET_TOKEN_ENDPOINT,
            "",
            callback
        )
    }

    private fun getCheckoutConfig() = VGSCheckoutMultiplexingConfig(
        token = accessToken,
        vaultID = vaultId
    )

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        if (result is VGSCheckoutResult.Canceled) {
            refreshToken { code, body ->
                if (code.isSuccessHttpCode()) accessToken = parseToken(body)
            }
        } else {
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

    internal fun tryAgainCheckout() {
        refreshToken { code, body ->
            if (code.isSuccessHttpCode()) accessToken = parseToken(body)

            checkout.present(getCheckoutConfig())
        }
    }

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private fun parseToken(body: String?) = body?.let {
            JsonParser
                .parseString(it)
                .asJsonObject
                .run {
                    takeIf { this.has(ACCESS_TOKEN) }
                        ?.run { get(ACCESS_TOKEN).asString } ?: ""
                }
        } ?: ""
    }
}