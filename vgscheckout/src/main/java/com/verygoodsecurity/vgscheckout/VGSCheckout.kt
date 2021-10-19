package com.verygoodsecurity.vgscheckout

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutTransitionOptions

/**
 *  A drop-in class that presents a checkout form.
 */
class VGSCheckout internal constructor(
    private val activityResultLauncher: ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfig>>
) {

    /**
     *  Constructor to be used when launching the checkout from activity.
     *
     *  @param activity that starts checkout.
     *  @param callback called with the result of checkout.
     */
    @JvmOverloads
    constructor(activity: AppCompatActivity, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(activity, callback)
    )

    /**
     *  Constructor to be used when launching the checkout from fragment.
     *
     *  @param fragment that starts checkout.
     *  @param callback called with the result of checkout.
     */
    @JvmOverloads
    constructor(fragment: Fragment, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(fragment, callback)
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
     * @param token client backend access token.
     * @param vaultID VGS vault id.
     * @param environment type of vault to communicate with.
     * @param transitionOptions specifying a custom animation to run when the checkout is displayed.
     * @param isAnalyticsEnabled true if checkout should send analytics events, false otherwise.
     *
     * @throws com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException if token is not valid.
     * @throws com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException if
     * token is contains restricted roles.
     */
    @JvmOverloads
    @Throws(VGSCheckoutInvalidJwtException::class, VGSCheckoutJwtRestrictedRoleException::class)
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
            activity: AppCompatActivity,
            callback: VGSCheckoutCallback?
        ) = activity.registerForActivityResult(CheckoutResultContract()) {
            callback?.onCheckoutResult(it)
        }

        private fun registerActivityLauncher(
            fragment: Fragment,
            callback: VGSCheckoutCallback?
        ) = fragment.registerForActivityResult(CheckoutResultContract()) {
            callback?.onCheckoutResult(it)
        }
    }
}