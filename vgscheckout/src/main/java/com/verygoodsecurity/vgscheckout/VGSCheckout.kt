package com.verygoodsecurity.vgscheckout

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutTransitionOptions
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable

/**
 *  A drop-in class that presents a checkout form.
 */
class VGSCheckout internal constructor(
    private val activityResultLauncher: ActivityResultLauncher<CheckoutResultContract.Args<CheckoutConfig>>
) {

    var onCheckoutInitListener: VGSCheckoutOnInitListener? = null

    private lateinit var activity: Activity
    private var callback: VGSCheckoutCallback? = null

    /**
     *  Constructor to be used when launching the checkout from activity.
     *
     *  @param activity that starts checkout.
     *  @param callback called with the result of checkout.
     */
    @JvmOverloads
    constructor(activity: AppCompatActivity, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(activity, callback)
    ) {
        this.activity = activity
        this.callback = callback
    }

    /**
     *  Constructor to be used when launching the checkout from fragment.
     *
     *  @param fragment that starts checkout.
     *  @param callback called with the result of checkout.
     */
    @JvmOverloads
    constructor(fragment: Fragment, callback: VGSCheckoutCallback? = null) : this(
        registerActivityLauncher(fragment, callback)
    ) {
        this.activity = fragment.requireActivity()
        this.callback = callback
    }

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
    ): VGSCheckoutCancellable? {
        return when (config) {
            is VGSCheckoutCustomConfig -> initializeCustomCheckout(config, transitionOptions)
            is VGSCheckoutAddCardConfig -> initializeAddCardCheckout(config, transitionOptions)
            else -> null
        }
    }

    private fun initializeCustomCheckout(
        config: VGSCheckoutCustomConfig,
        transitionOptions: VGSCheckoutTransitionOptions?
    ): VGSCheckoutCancellable? {
        startCheckoutForm(config, transitionOptions)
        return null
    }

    private fun initializeAddCardCheckout(
        config: VGSCheckoutAddCardConfig,
        transitionOptions: VGSCheckoutTransitionOptions? = null
    ): VGSCheckoutCancellable {
        return VGSCheckoutAddCardConfig.loadSavedCards(
            activity,
            config,
            object : VGSCheckoutConfigInitCallback {
                override fun onSuccess() {
                    startCheckoutForm(config, transitionOptions)
                }

                override fun onFailure(exception: VGSCheckoutException) {
                    onCheckoutInitListener?.onCheckoutInitializationFailure(exception)
                }
            }
        )
    }

    private fun startCheckoutForm(
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