package com.verygoodsecurity.vgscheckout

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
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