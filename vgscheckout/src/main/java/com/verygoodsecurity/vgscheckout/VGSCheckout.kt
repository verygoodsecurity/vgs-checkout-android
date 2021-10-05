package com.verygoodsecurity.vgscheckout

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutTransitionOptions

class VGSCheckout internal constructor(
    private val activityResultLauncher: ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfig>>
) {

    @JvmOverloads
    constructor(activity: ComponentActivity, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(activity, callback)
    )

    /**
     * Start checkout.
     *
     * @param config specifying checkout form, networking configuration.
     * @param transitionOptions specifying a custom animation to run when the checkout is displayed.
     */
    @JvmOverloads
    fun present(
        config: CheckoutConfig,
        transitionOptions: VGSCheckoutTransitionOptions? = null
    ) {
        activityResultLauncher.launch(
            CheckoutResultContract.Args(config),
            transitionOptions?.options
        )
    }

    /**
     * Start checkout with multiplexing configuration.
     *
     * @throws IllegalArgumentException if token is not valid.
     *
     * @param token client backend access token.
     * @param vaultID VGS vault id.
     * @param environment type of vault to communicate with.
     * @param transitionOptions specifying a custom animation to run when the checkout is displayed.
     * @param isAnalyticsEnabled true if checkout should send analytics events, false otherwise.
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun present(
        token: String,
        vaultID: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        transitionOptions: VGSCheckoutTransitionOptions? = null,
        isAnalyticsEnabled: Boolean = true
    ) {
        present(
            VGSCheckoutMultiplexingConfig(
                token,
                vaultID,
                environment,
                isAnalyticsEnabled = isAnalyticsEnabled
            ),
            transitionOptions
        )
    }

    private companion object {

        private fun registerActivityLauncher(
            activity: ComponentActivity,
            callback: VGSCheckoutCallback?
        ) = activity.registerForActivityResult(CheckoutResultContract()) {
            callback?.onCheckoutResult(it)
        }
    }
}