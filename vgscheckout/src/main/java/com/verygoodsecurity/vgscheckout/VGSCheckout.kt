package com.verygoodsecurity.vgscheckout

import android.app.Activity
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity

private const val DEFAULT_ENVIRONMENT = "sandbox"

class VGSCheckout constructor(
    private val vaultID: String,
    private val environment: String = DEFAULT_ENVIRONMENT,
) {

    fun present(activity: Activity, requestCode: Int, config: VGSCheckoutVaultConfiguration) {
        CheckoutActivity.startForResult(activity, requestCode, vaultID, environment, config)
    }
}