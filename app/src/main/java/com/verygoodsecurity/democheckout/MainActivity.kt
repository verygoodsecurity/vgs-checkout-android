package com.verygoodsecurity.democheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
=import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.democheckout.util.extension.showShort
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCityAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.region.VGSCheckoutRegionOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity() {

    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleCheckoutResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            VGSCheckout().present(this, 1, getCheckoutConfig())
        }
    }

    private fun handleCheckoutResult(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> {
                val result = activityResult.data?.getParcelableExtra<VGSCheckoutResult>(
                    CHECKOUT_RESULT_EXTRA_KEY
                )
                showShort("Checkout complete: code = ${result?.code}, message = ${result?.body}")
            }
            Activity.RESULT_CANCELED -> showShort("Checkout canceled")
        }
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

    private fun getAddressOptions() = VGSCheckoutAddressOptions.Builder()
        .setCountryOptions(
            VGSCheckoutCountryOptions.Builder()
                .setFieldName("address.country")
                .build()
        )
        .setRegionOptions(
            VGSCheckoutRegionOptions.Builder().setFieldName("address_info.region").build()
        )
        .setCityOptions(
            VGSCheckoutCityOptions.Builder().setFieldName("address_info.city").build()
        )
        .setAddressOptions(
            VGSCheckoutCityAddressOptions.Builder()
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