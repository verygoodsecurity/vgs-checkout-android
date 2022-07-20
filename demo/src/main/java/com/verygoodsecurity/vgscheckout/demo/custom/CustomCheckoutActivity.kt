package com.verygoodsecurity.vgscheckout.demo.custom

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.demo.BaseActivity
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.CheckoutType
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

class CustomCheckoutActivity : BaseActivity(R.layout.activity_custom_checkout),
    VGSCheckoutCallback {

    override val type: CheckoutType = CheckoutType.CUSTOM

    // Important: Best place to init checkout object is onCreate
    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        checkout.present(createConfig())
    }

    private fun createConfig(): VGSCheckoutCustomConfig {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val vaultId = preferences.getString(getString(R.string.setting_key_vault_id), null)
            ?: BuildConfig.STORAGE_ID

        val builder = VGSCheckoutCustomConfig.Builder(vaultId)

        // Create for config, configure UI and setup fieldNames
        val billingAddressVisibility =
            if (preferences.getBoolean(R.string.setting_key_billing_address_visible)) {
                VGSCheckoutBillingAddressVisibility.VISIBLE
            } else {
                VGSCheckoutBillingAddressVisibility.HIDDEN
            }
        builder.setCardNumberOptions(
            CARD_NUMBER_FIELD_NAME,
            !preferences.getBoolean(R.string.setting_key_card_number_icon_visible)
        ).setCardHolderOptions(
            CARD_HOLDER_FIELD_NAME,
            preferences.getFieldVisibility(R.string.setting_key_card_holder_visible)
        ).setCVCOptions(
            CVC_FIELD_NAME,
            !preferences.getBoolean(R.string.setting_key_cvc_icon_visible)
        ).setExpirationDateOptions(
            EXPIRY_FIELD_NAME
        ).setCountryOptions(
            COUNTRY_FIELD_NAME,
            visibility = preferences.getFieldVisibility(R.string.setting_key_country_visible)
        ).setCityOptions(
            CITY_FIELD_NAME,
            preferences.getFieldVisibility(R.string.setting_key_city_visible)
        ).setAddressOptions(
            ADDRESS_FIELD_NAME,
            preferences.getFieldVisibility(R.string.setting_key_address_visible)
        ).setOptionalAddressOptions(
            OPTIONAL_ADDRESS_FIELD_NAME,
            preferences.getFieldVisibility(R.string.setting_key_optional_address_visible)
        ).setPostalCodeOptions(
            POSTAL_CODE_FIELD_NAME,
            preferences.getFieldVisibility(R.string.setting_key_postal_code_visible)
        ).setBillingAddressVisibility(billingAddressVisibility)
            .setFormValidationBehaviour(preferences.getValidationBehaviour(R.string.setting_key_validation_behaviour))

        // Create route config, specify path, extra data, headers etc.
        builder.setPath(PATH)
            .setPayload(
                linkedMapOf(
                    EXTRA_DATA_ROOT to mapOf(
                        EXTRA_DATA_CARD_DATA to arrayOf(
                            null,
                            mapOf(EXTRA_DATA_CVC_KEY to EXTRA_DATA_CVC_VALUE),
                            null
                        )
                    )
                )
            ).setMergePolicy(VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE)

        // Create config object
        return builder.build()
    }

    companion object {

        private const val CARD_NUMBER_FIELD_NAME = "data.card_data[1]"
        private const val CARD_HOLDER_FIELD_NAME = "card_data.card_holder"
        private const val EXPIRY_FIELD_NAME = "card_data.exp_date"
        private const val CVC_FIELD_NAME = "card_data.card_cvc"

        private const val COUNTRY_FIELD_NAME = "data.billing_address.country"
        private const val ADDRESS_FIELD_NAME = "data.billing_address.address"
        private const val OPTIONAL_ADDRESS_FIELD_NAME = "data.billing_address.optional_address"
        private const val CITY_FIELD_NAME = "data.billing_address.city"
        private const val POSTAL_CODE_FIELD_NAME = "data.billing_address.postal_code"

        private const val PATH = "post"
        private const val EXTRA_DATA_ROOT = "data"
        private const val EXTRA_DATA_CARD_DATA = "card_data"
        private const val EXTRA_DATA_CVC_KEY = "cvc"
        private const val EXTRA_DATA_CVC_VALUE = "333"
    }
}
