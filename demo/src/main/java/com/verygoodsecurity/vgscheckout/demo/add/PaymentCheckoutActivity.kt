package com.verygoodsecurity.vgscheckout.demo.add

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import okhttp3.*
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException

class PaymentCheckoutActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val tokenManager: TokenManager = TokenManager()

    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_checkout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPresent).setOnClickListener { presentCheckout() }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        // Handle checkout result
        Log.d(
            this::class.simpleName, """
            ${result::class.java.simpleName}
            ${result.data.getParcelable<VGSCheckoutCardResponse>(VGSCheckoutResultBundle.ADD_CARD_RESPONSE)}
        """.trimIndent()
        )
    }

    private fun presentCheckout() {
        tokenManager.get {
            if (it?.value == null) {
                Log.d(this::class.simpleName, "Token is null.")
                return@get
            }
            checkout.present(createConfig(it.value))
        }
    }

    private fun createConfig(token: String) =
        VGSCheckoutAddCardConfig(token, BuildConfig.STORAGE_ID)
}

class TokenManager {

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()

    private val tokenRequest: Request by lazy {
        Request.Builder()
            .url(BuildConfig.CLIENT_HOST + BuildConfig.GET_TOKEN_ENDPOINT)
            .post(EMPTY_REQUEST)
            .build()
    }

    fun get(onResult: ((token: Token?) -> Unit)?) {
        try {
            client.newCall(tokenRequest).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    onResult?.invoke(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    onResult?.invoke(parserResponse(response))
                }
            })
        } catch (e: Exception) {
            onResult?.invoke(null)
        }
    }

    private fun parserResponse(response: Response): Token? {
        return with(response.body?.string()) {
            if (this == null) {
                null
            } else {
                Gson().fromJson(this, Token::class.java)
            }
        }
    }

    @JvmInline
    value class Token constructor(@SerializedName("access_token") val value: String)
}