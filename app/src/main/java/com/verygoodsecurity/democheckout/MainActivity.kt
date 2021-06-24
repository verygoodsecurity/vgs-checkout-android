package com.verygoodsecurity.democheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.democheckout.util.extension.showShort
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.mbBasicFlow).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.mbMultiplexingFlow).setOnClickListener(this)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = data?.getParcelableExtra<VGSCheckoutResult>(CHECKOUT_RESULT_EXTRA_KEY)
            showShort("Checkout complete: code = ${result?.code}, message = ${result?.body}")
        } else if (resultCode == Activity.RESULT_CANCELED) {
            showShort("Checkout canceled")
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.mbBasicFlow -> VGSCheckout().present(this, 1, getCheckoutConfig())
            R.id.mbMultiplexingFlow -> VGSCheckout().present(this, 1, getMultiplexingConfig())
        }
    }

    private fun getCheckoutConfig(): VGSCheckoutConfiguration {
        val routeConfig = VGSCheckoutRouteConfiguration.Builder()
            .setPath("post")
            .build()

        val formConfig = VGSCheckoutFormConfiguration.Builder()
            .setCardHolderOptions(
                VGSCheckoutCardHolderOptions.Builder()
                    .setFieldName("card_data.personal_data.cardHolder")
                    .build()
            )
            .setCardNumberOptions(
                VGSCheckoutCardNumberOptions.Builder()
                    .setFieldName("cardNumber")
                    .setCardBrands(
                        VGSCheckoutCardBrand.Elo(
                            icon = R.drawable.ic_amex_light,
                            mask = "# # # # # # # # # # # # # # # #"
                        ),
                        VGSCheckoutCardBrand.Custom(
                            "Custom brand",
                            R.drawable.ic_amex_dark,
                            "^001",
                            "## ## ## ## ## ## ## ##",
                            arrayOf(16),
                            arrayOf(3),
                            VGSCheckoutChecksumAlgorithm.LUHN
                        )
                    )
                    .build()
            )
            .setExpirationDateOptions(
                VGSCheckoutExpirationDateOptions.Builder()
                    .setFieldName("ard_data.personal_data.secret.expDate")
                    .build()
            )
            .setCVCOptions(
                VGSCheckoutCVCOptions.Builder()
                    .setFieldName("card_data.cardCvc")
                    .build()
            )
            .build()

        return VGSCheckoutConfiguration.Builder("tntpszqgikn")
            .setRouteConfig(routeConfig)
            .setFormConfig(formConfig)
            .build()
    }

    private fun getMultiplexingConfig() =
        VGSCheckoutMultiplexingConfiguration.Builder("tntpszqgikn").build()
}