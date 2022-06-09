package com.verygoodsecurity.vgscheckout.demo.orchestrtion.payment

import androidx.preference.PreferenceManager
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutPaymentFormConfig
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
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

class PaymentCheckoutActivity : OrchestrationCheckoutActivity() {

    override fun initializeConfiguration(
        token: String,
        callback: (config: CheckoutConfig) -> Unit
    ) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val formConfig = VGSCheckoutPaymentFormConfig(
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

        VGSCheckoutPaymentConfig.create(
            this,
            token,
            BuildConfig.TEMPORARY_ORDER_ID,
            BuildConfig.STORAGE_ID,
            VGSCheckoutPaymentMethod.SavedCards(
                arrayListOf(BuildConfig.TEMPORARY_FIN_INSTRUMENT)
            ),
            VGSCheckoutEnvironment.Sandbox(),
            formConfig,
            false,
            true,
            object : VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig> {
                override fun onSuccess(config: VGSCheckoutPaymentConfig) {
                    callback(config)
                }

                override fun onFailure(exception: VGSCheckoutException) {
                    TODO("Not yet implemented because it never used for now")
                }
            }
        )
    }
}