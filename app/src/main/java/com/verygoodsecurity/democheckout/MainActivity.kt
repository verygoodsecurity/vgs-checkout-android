package com.verygoodsecurity.democheckout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutVaultRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutVaultFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.VGSCheckoutExpirationDateOptions

class MainActivity : AppCompatActivity() {

    // TODO: Ask about contentPath vs fieldName (IMPORTANT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val routeConfig = VGSCheckoutVaultRouteConfiguration.Builder()
            .setPath("post")
            .build()

        val formConfig = VGSCheckoutVaultFormConfiguration.Builder()
            .setCardHolderOptions(
                VGSCheckoutCardHolderOptions.Builder()
                    .setContentPath("card_data.personal_data.cardHolder")
                    .build()
            )
            .setCardNumberOptions(
                VGSCheckoutCardNumberOptions.Builder()
                    .setContentPath("cardNumber")
                    .build()
            )
            .setExpirationDateOptions(
                VGSCheckoutExpirationDateOptions.Builder()
                    .setContentPath("ard_data.personal_data.secret.expDate")
                    .build()
            )
            .setCVCOptions(
                VGSCheckoutCVCOptions.Builder()
                    .setContentPath("card_data.cardCvc")
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
}