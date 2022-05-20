package com.verygoodsecurity.vgscheckout.demo.payment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutPaymentCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPaymentPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import com.verygoodsecurity.vgscheckout.demo.BaseActivity
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.CheckoutType
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import okhttp3.*
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException

class PaymentCheckoutActivity : BaseActivity(R.layout.activity_payment_checkout),
    VGSCheckoutCallback {

    override val type: CheckoutType = CheckoutType.PAYMENT

    private val tokenManager: TokenManager = TokenManager()

    // Important: Best place to init checkout object is onCreate
    private lateinit var checkout: VGSCheckout

    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val mbPresent: MaterialButton by lazy { findViewById(R.id.mbPresent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkout = VGSCheckout(this, this)
        mbPresent.setOnClickListener { presentCheckout() }
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
        setLoading(true)
        tokenManager.get {
            setLoading(false)
            if (it?.value == null) {
                Log.d(this::class.simpleName, "Token is null.")
                return@get
            }
            checkout.present(createConfig(it.value))
        }
    }

    private fun createConfig(token: String): VGSCheckoutAddCardConfig {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val formConfig = VGSCheckoutAddCardFormConfig(
            VGSCheckoutPaymentBillingAddressOptions(
                VGSCheckoutPaymentCountryOptions(
                    visibility = preferences.getFieldVisibility(R.string.setting_key_country_visible)
                ),
                VGSCheckoutPaymentCityOptions(preferences.getFieldVisibility(R.string.setting_key_city_visible)),
                VGSCheckoutPaymentAddressOptions(preferences.getFieldVisibility(R.string.setting_key_address_visible)),
                VGSCheckoutPaymentOptionalAddressOptions(preferences.getFieldVisibility(R.string.setting_key_optional_address_visible)),
                VGSCheckoutPaymentPostalCodeOptions(preferences.getFieldVisibility(R.string.setting_key_postal_code_visible)),
                if (preferences.getBoolean(R.string.setting_key_billing_address_visible)) {
                    VGSCheckoutBillingAddressVisibility.VISIBLE
                } else {
                    VGSCheckoutBillingAddressVisibility.HIDDEN
                }
            ),
            preferences.getValidationBehaviour(R.string.setting_key_validation_behaviour),
            preferences.getBoolean(R.string.setting_key_save_card_option_enabled)
        )
        val vaultId = preferences.getString(getString(R.string.setting_key_vault_id), null)
        return VGSCheckoutAddCardConfig(
            accessToken = token,
            tenantId = vaultId ?: BuildConfig.STORAGE_ID,
            formConfig = formConfig
        )
    }

    private fun setLoading(isLoading: Boolean) {
        mbPresent.isEnabled = !isLoading
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

class TokenManager {

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val tokenRequest: Request by lazy {
        Request.Builder()
            .url("https://multiplexing-demo.verygoodsecurity.io/get-auth-token")
            .post(EMPTY_REQUEST)
            .build()
    }

    fun get(onResult: ((token: Token?) -> Unit)?) {
        try {
            client.newCall(tokenRequest).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    mainThreadHandler.post { onResult?.invoke(null) }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainThreadHandler.post { onResult?.invoke(parserResponse(response)) }
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