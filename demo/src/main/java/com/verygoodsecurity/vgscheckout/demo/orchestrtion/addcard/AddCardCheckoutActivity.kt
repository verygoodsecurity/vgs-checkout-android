package com.verygoodsecurity.vgscheckout.demo.orchestrtion.addcard

import androidx.preference.PreferenceManager
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
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
        val vaultId = preferences.getString(getString(R.string.setting_key_vault_id), null)
            ?: BuildConfig.STORAGE_ID

        val builder = VGSCheckoutAddCardConfig.Builder(vaultId)
            .setAccessToken(token)
            .setSavedCardIds(arrayListOf(BuildConfig.TEMPORARY_FIN_INSTRUMENT))

        // Create for config, configure UI and setup fieldNames
        val billingAddressVisibility =
            if (preferences.getBoolean(R.string.setting_key_billing_address_visible)) {
                VGSCheckoutBillingAddressVisibility.VISIBLE
            } else {
                VGSCheckoutBillingAddressVisibility.HIDDEN
            }

        builder.setCountryOptions(
            preferences.getFieldVisibility(R.string.setting_key_country_visible)
        ).setCityOptions(
            preferences.getFieldVisibility(R.string.setting_key_city_visible)
        ).setAddressOptions(
            preferences.getFieldVisibility(R.string.setting_key_address_visible)
        ).setOptionalAddressOptions(
            preferences.getFieldVisibility(R.string.setting_key_optional_address_visible)
        ).setPostalCodeOptions(
            preferences.getFieldVisibility(R.string.setting_key_postal_code_visible)
        ).setBillingAddressVisibility(billingAddressVisibility)
            .setFormValidationBehaviour(preferences.getValidationBehaviour(R.string.setting_key_validation_behaviour))
            .setIsSaveCardOptionVisible(preferences.getBoolean(R.string.setting_key_save_card_option_enabled))

        // Create config object
        return builder.build()
    }
}