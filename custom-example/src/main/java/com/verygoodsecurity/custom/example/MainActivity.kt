package com.verygoodsecurity.custom.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.custom.R
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String by lazy { getString(R.string.vault_id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            checkout.present(getCheckoutConfig())
        }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        Log.d("VGSCheckout", result.toString())
    }

    //region Checkout config
    private fun getCheckoutConfig() = VGSCheckoutCustomConfig(
        vaultID = vaultId,
        routeConfig = getCheckoutRouteConfig(),
        formConfig = getCheckoutFormConfig()
    )

    private fun getCheckoutRouteConfig() = VGSCheckoutCustomRouteConfig("post")

    private fun getCheckoutFormConfig() =
        VGSCheckoutCustomFormConfig(getCardOptions(), getAddressOptions())

    private fun getCardOptions() = VGSCheckoutCustomCardOptions(
        VGSCheckoutCardNumberOptions("card_data.card_number"),
        VGSCheckoutCardHolderOptions("card_data.card_holder"),
        VGSCheckoutCVCOptions("card_data.card_cvc"),
        VGSCheckoutExpirationDateOptions("card_data.exp_date")
    )

    private fun getAddressOptions() = VGSCheckoutCustomBillingAddressOptions(
        VGSCheckoutCountryOptions("address_info.country"),
        VGSCheckoutCityOptions("address_info.city"),
        VGSCheckoutAddressOptions("address_info.address"),
        postalAddressOptions = VGSCheckoutPostalAddressOptions("address_info.postal_address"),
    )
    //endregion
}