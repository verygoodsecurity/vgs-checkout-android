package com.verygoodsecurity.democheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.democheckout.util.extension.showShort
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleCheckoutResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.mbBasicFlow).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.mbMultiplexingFlow).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mbBasicFlow -> VGSCheckout().present(
                this,
                activityLauncher,
                getCheckoutConfig()
            )
            R.id.mbMultiplexingFlow -> VGSCheckout().present(
                this,
                activityLauncher,
                getMultiplexingConfig(),
            )
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
        .setPayButtonTitle("10$")
        .setCardHolderOptions(getCardHolderOptions())
        .setCardNumberOptions(getCardNumberOptions())
        .setExpirationDateOptions(getExpirationDateOptions())
        .setCVCOptions(getCVCOptions())
        .build()

    private fun getCardHolderOptions() = VGSCheckoutCardHolderOptions.Builder()
        .setFieldName("card_data.personal_data.cardHolder")
        .build()

    private fun getCardNumberOptions() = VGSCheckoutCardNumberOptions.Builder()
        .setFieldName("cardNumber")
        .build()

    private fun getExpirationDateOptions() = VGSCheckoutExpirationDateOptions.Builder()
        .setFieldName("ard_data.personal_data.secret.expDate")
        .build()

    private fun getCVCOptions() = VGSCheckoutCVCOptions.Builder()
        .setFieldName("card_data.cardCvc")
        .build()
    //endregion

    //region Checkout multiplexing config
    private fun getMultiplexingConfig() =
        VGSCheckoutMultiplexingConfiguration.Builder("tntpszqgikn")
            .setFormConfig(
                VGSCheckoutMultiplexingFormConfiguration.Builder()
                    .setPayButtonTitle("11.99$").build()
            )
            .build()
    //endregion
}