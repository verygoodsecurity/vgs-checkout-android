package com.verygoodsecurity.vgscheckout

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract

class VGSCheckout internal constructor(
    private val activityResultLauncher: ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfiguration>>
) {

    @JvmOverloads
    constructor(activity: ComponentActivity, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(activity, callback)
    )

    fun present(config: CheckoutConfiguration) {
        activityResultLauncher.launch(CheckoutResultContract.Args(config))
    }

    companion object {

        private fun registerActivityLauncher(
            activity: ComponentActivity,
            callback: VGSCheckoutCallback?
        ): ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfiguration>> {
            return activity.registerForActivityResult(CheckoutResultContract()) {
                callback?.onCheckoutResult(it)
            }
        }
    }
}