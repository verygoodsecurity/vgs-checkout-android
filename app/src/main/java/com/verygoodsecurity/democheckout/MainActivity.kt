package com.verygoodsecurity.democheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
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

class MainActivity : AppCompatActivity() {

    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleCheckoutResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            VGSCheckout().present(this, activityLauncher, getCheckoutConfig())
        }
    }

    private fun handleCheckoutResult(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            Activity.RESULT_OK -> {
                val result = activityResult.data?.getParcelableExtra<VGSCheckoutResult>(
                    CHECKOUT_RESULT_EXTRA_KEY
                )
                showShort("Checkout complete: code = ${result?.code}")
            }
            Activity.RESULT_CANCELED -> showShort("Checkout canceled")
        }
    }

    //region Checkout config
    private fun getCheckoutConfig() = VGSCheckoutConfiguration(
        vaultID = "tntpszqgikn",
        routeConfig = getCheckoutRouteConfig(),
        formConfig = getCheckoutFormConfig()
    )

    private fun getCheckoutRouteConfig() = VGSCheckoutRouteConfiguration("post")

    private fun getCheckoutFormConfig() =
        VGSCheckoutFormConfiguration(getCardOptions(), getAddressOptions(), "9.73$")

    private fun getCardOptions() = VGSCheckoutCardOptions(
        VGSCheckoutCardNumberOptions.Builder()
            .setFieldName("card_data.card_number")
            .build(),
        VGSCheckoutCardHolderOptions("card_data.card_holder"),
        VGSCheckoutCVCOptions("card_data.card_cvc"),
        VGSCheckoutExpirationDateOptions("card_data.exp_date")
    )

    private fun getAddressOptions() = VGSCheckoutBillingAddressOptions(
        VGSCheckoutCountryOptions("address_info.country"),
        VGSCheckoutCityOptions("address_info.city"),
        VGSCheckoutAddressOptions("address_info.address"),
        postalAddressOptions = VGSCheckoutPostalAddressOptions("address_info.postal_address"),
    )
    //endregion
}