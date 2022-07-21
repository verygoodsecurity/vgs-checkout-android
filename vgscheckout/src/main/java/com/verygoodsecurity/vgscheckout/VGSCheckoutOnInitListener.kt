package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

/**
 * Interface definition for callbacks that are invoked when VGS Checkout initialization succeeds or fails.
 */
interface VGSCheckoutOnInitListener {

    /**
     * Called when initialization of the player succeeds.
     */
    fun onCheckoutInitializationSuccess()

    /**
     * Called when initialization of the player fails.
     *
     * @param exception Indicates a reason of initialization failure.
     */
    fun onCheckoutInitializationFailure(exception: VGSCheckoutException)
}