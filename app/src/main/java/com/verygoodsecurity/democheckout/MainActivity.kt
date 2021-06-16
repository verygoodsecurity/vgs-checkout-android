package com.verygoodsecurity.democheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutVaultRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutVaultFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val routeConfig = VGSCheckoutVaultRouteConfiguration.Builder()
            .setPath("post")
            .build()

        val formConfig = VGSCheckoutVaultFormConfiguration.Builder()
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

        val config = VGSCheckoutVaultConfiguration.Builder()
            .setRouteConfig(routeConfig)
            .setFormConfig(formConfig)
            .build()

        findViewById<TextView>(R.id.tvWelcome).setOnClickListener {
            VGSCheckout("tntpszqgikn").present(this, 1, config)
        }
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

    private fun showShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}