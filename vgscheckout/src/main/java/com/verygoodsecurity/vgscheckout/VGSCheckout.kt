package com.verygoodsecurity.vgscheckout

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract

class VGSCheckout internal constructor(
    private val activityResultLauncher: ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfiguration>>
) {

    constructor(activity: ComponentActivity, callback: VGSCheckoutCallback) : this(
        registerActivityLauncher(activity, callback)
    )

    fun present(config: CheckoutConfiguration) {
        activityResultLauncher.launch(CheckoutResultContract.Args(config))
    }

    /**
     * Start checkout with multiplexing configuration.
     *
     * @param token client backend access token.
     * @param vaultID VGS vault id.
     * @param environment type of vault to communicate with.
     * @param isAnalyticsEnabled true if checkout should send analytics events, false otherwise.
     */
    @JvmOverloads
    fun present(
        token: String,
        vaultID: String,
        environment: String = DEFAULT_ENVIRONMENT,
        isAnalyticsEnabled: Boolean = true
    ) {
        activityResultLauncher.launch(
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfiguration(
                    token,
                    vaultID,
                    environment,
                    isAnalyticsEnabled
                )
            )
        )
    }

    companion object {

        private fun registerActivityLauncher(
            activity: ComponentActivity,
            callback: VGSCheckoutCallback
        ): ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfiguration>> {
            return activity.registerForActivityResult(CheckoutResultContract()) {
                callback.onCheckoutResult(it)
            }
        }
    }
}