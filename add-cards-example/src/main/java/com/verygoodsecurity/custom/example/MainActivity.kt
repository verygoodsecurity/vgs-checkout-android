package com.verygoodsecurity.custom.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.R
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCustomCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutCustomPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCustomCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCustomCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCustomCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutCustomExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse

class MainActivity : AppCompatActivity(), VGSCheckoutCallback {

    private val vaultId: String by lazy { getString(R.string.storage_id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPay).setOnClickListener {
            checkout.present(getCheckoutConfig())
        }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        val resultData: VGSCheckoutResultBundle? = when (result) {
            is VGSCheckoutResult.Success -> result.data
            is VGSCheckoutResult.Failed -> result.data
            else -> null
        }
        val addCardResponse =
            resultData?.getParcelable<VGSCheckoutAddCardResponse>(VGSCheckoutResultBundle.ADD_CARD_RESPONSE)
        Log.d(
            "VGSCheckout", """
            ${result::class.java.simpleName}
            Add card response = $addCardResponse
        """.trimIndent()
        )
    }

    //region Checkout config
    private fun getCheckoutConfig() = VGSCheckoutCustomConfig(
        vaultId = vaultId,
        routeConfig = getCheckoutRouteConfig(),
        formConfig = getCheckoutFormConfig()
    )

    private fun getCheckoutRouteConfig() = VGSCheckoutCustomRouteConfig(
        "post",
        requestOptions = VGSCheckoutCustomRequestOptions(
            extraData = linkedMapOf(
                "data" to mapOf(
                    "card_data" to arrayOf(
                        null,
                        mapOf("cvc" to "333"),
                        null
                    )
                )
            ),
            mergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE
        )
    )

    private fun getCheckoutFormConfig() =
        VGSCheckoutCustomFormConfig(getCardOptions(), getAddressOptions())

    private fun getCardOptions() = VGSCheckoutCustomCardOptions(
        VGSCheckoutCustomCardNumberOptions("data.card_data[1]"),
        VGSCheckoutCustomCardHolderOptions("card_data.card_holder"),
        VGSCheckoutCustomCVCOptions("card_data.card_cvc"),
        VGSCheckoutCustomExpirationDateOptions("card_data.exp_date")
    )

    private fun getAddressOptions() = VGSCheckoutCustomBillingAddressOptions(
        VGSCheckoutCustomCountryOptions("address_info.country"),
        VGSCheckoutCustomCityOptions("address_info.city"),
        VGSCheckoutCustomAddressOptions("address_info.address"),
        postalCodeOptions = VGSCheckoutCustomPostalCodeOptions("address_info.postal_code"),
    )
    //endregion
}