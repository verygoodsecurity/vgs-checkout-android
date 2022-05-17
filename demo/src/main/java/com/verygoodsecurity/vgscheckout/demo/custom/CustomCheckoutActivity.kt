package com.verygoodsecurity.vgscheckout.demo.custom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.VGSCheckout
import com.verygoodsecurity.vgscheckout.VGSCheckoutCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCustomCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCustomCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCustomCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutCustomExpirationDateOptions
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.R
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse

class CustomCheckoutActivity : AppCompatActivity(), VGSCheckoutCallback {

    // Important: Best place to init checkout object is onCreate
    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_checkout)
        checkout = VGSCheckout(this, this)
        findViewById<MaterialButton>(R.id.mbPresent).setOnClickListener { presentCheckout() }
    }

    override fun onCheckoutResult(result: VGSCheckoutResult) {
        // Handle checkout result
        Log.d(
            this::class.simpleName, """
            ${result::class.java.simpleName}
            ${result.data.getParcelable<VGSCheckoutCardResponse>(VGSCheckoutResultBundle.ADD_CARD_RESPONSE)}
        """.trimIndent()
        )
    }

    private fun presentCheckout() {
        checkout.present(createConfig())
    }

    private fun createConfig(): VGSCheckoutCustomConfig {
        // Create for config, configure UI and setup fieldNames
        val formConfig = VGSCheckoutCustomFormConfig(
            VGSCheckoutCustomCardOptions(
                VGSCheckoutCustomCardNumberOptions(CARD_NUMBER_FIELD_NAME),
                VGSCheckoutCustomCardHolderOptions(CARD_HOLDER_FIELD_NAME),
                VGSCheckoutCustomCVCOptions(CVC_FIELD_NAME),
                VGSCheckoutCustomExpirationDateOptions(EXPIRY_FIELD_NAME)
            )
        )

        // Create route config, specify path, extra data, headers etc.
        val routeConfig = VGSCheckoutCustomRouteConfig(
            PATH,
            requestOptions = VGSCheckoutCustomRequestOptions(
                extraData = linkedMapOf(
                    EXTRA_DATA_ROOT to mapOf(
                        EXTRA_DATA_CARD_DATA to arrayOf(
                            null,
                            mapOf(EXTRA_DATA_CVC_KEY to EXTRA_DATA_CVC_VALUE),
                            null
                        )
                    )
                ),
                mergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE
            )
        )

        // Create config object
        return VGSCheckoutCustomConfig(
            vaultId = BuildConfig.STORAGE_ID,
            routeConfig = routeConfig,
            formConfig = formConfig
        )
    }

    companion object {

        private const val CARD_NUMBER_FIELD_NAME = "data.card_data[1]"
        private const val CARD_HOLDER_FIELD_NAME = "card_data.card_holder"
        private const val EXPIRY_FIELD_NAME = "card_data.exp_date"
        private const val CVC_FIELD_NAME = "card_data.card_cvc"

        private const val PATH = "post"
        private const val EXTRA_DATA_ROOT = "data"
        private const val EXTRA_DATA_CARD_DATA = "card_data"
        private const val EXTRA_DATA_CVC_KEY = "cvc"
        private const val EXTRA_DATA_CVC_VALUE = "333"
    }
}