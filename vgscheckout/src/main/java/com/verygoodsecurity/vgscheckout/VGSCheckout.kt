package com.verygoodsecurity.vgscheckout

import android.app.Activity
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity

const val CHECKOUT_RESULT_EXTRA_KEY = "checkout_result_extra_key"

private const val DEFAULT_ENVIRONMENT = "sandbox"

class VGSCheckout constructor(
    private val vaultID: String,
    private val environment: String = DEFAULT_ENVIRONMENT,
) {

    fun present(activity: Activity, requestCode: Int, config: VGSCheckoutConfiguration) {
        CheckoutActivity.startForResult(activity, requestCode, vaultID, environment, config)
    }
}