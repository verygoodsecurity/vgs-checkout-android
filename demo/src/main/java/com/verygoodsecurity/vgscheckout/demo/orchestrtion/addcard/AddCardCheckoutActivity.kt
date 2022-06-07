package com.verygoodsecurity.vgscheckout.demo.orchestrtion.addcard

import androidx.preference.PreferenceManager
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutPaymentCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPaymentPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.demo.orchestrtion.OrchestrationCheckoutActivity

class AddCardCheckoutActivity : OrchestrationCheckoutActivity() {

    override fun initializeConfiguration(
        token: String,
        callback: (config: CheckoutConfig) -> Unit
    ) = callback(createConfig(token))

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

}