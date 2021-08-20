package com.verygoodsecurity.democheckout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.democheckout.util.extension.showShort
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity(), VGSCheckoutCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            checkout.present(getCheckoutConfig())
        }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        showShort(
            when (result) {
                is VGSCheckoutResult.Success -> "Checkout complete: body = ${result.body}"
                is VGSCheckoutResult.Failed -> "Checkout failed: code = ${result.code}, body = ${result.body}"
                is VGSCheckoutResult.Canceled -> "Checkout canceled"
            }
        )
    }

    //region Checkout config
    private fun getCheckoutConfig() = VGSCheckoutConfiguration.Builder("tntpszqgikn")
        .setRouteConfig(getCheckoutRouteConfig())
        .setFormConfig(getCheckoutFormConfig())
        .build()

    private fun getCheckoutRouteConfig() = VGSCheckoutRouteConfiguration.Builder()
        .setPath("post")
        .build()

    private fun getCheckoutFormConfig() = VGSCheckoutFormConfiguration.Builder()
        .setPayButtonTitle("9.73$")
        .setCardOptions(getCardOptions())
        .setAddressOptions(getAddressOptions())
        .build()

    private fun getCardOptions() = VGSCheckoutCardOptions.Builder()
        .setCardHolderOptions(
            VGSCheckoutCardHolderOptions.Builder()
                .setFieldName("card_data.card_holder")
                .build()
        )
        .setCardNumberOptions(
            VGSCheckoutCardNumberOptions.Builder()
                .setFieldName("card_data.card_number")
                .build()
        )
        .setExpirationDateOptions(
            VGSCheckoutExpirationDateOptions.Builder()
                .setFieldName("card_data.exp_date")
                .build()
        )
        .setCVCOptions(
            VGSCheckoutCVCOptions.Builder()
                .setFieldName("card_data.card_cvc")
                .build()
        )
        .build()

    private fun getAddressOptions() = VGSCheckoutBillingAddressOptions.Builder()
        .setCountryOptions(
            VGSCheckoutCountryOptions.Builder()
                .setFieldName("address_info.country")
                .build()
        )
        .setCityOptions(
            VGSCheckoutCityOptions.Builder().setFieldName("address_info.city").build()
        )
        .setAddressOptions(
            VGSCheckoutAddressOptions.Builder()
                .setFieldName("address_info.address")
                .build()
        )
        .setPostalAddressOptions(
            VGSCheckoutPostalAddressOptions.Builder()
                .setFieldName("address_info.postal_address")
                .build()
        )
        .build()
    //endregion
}