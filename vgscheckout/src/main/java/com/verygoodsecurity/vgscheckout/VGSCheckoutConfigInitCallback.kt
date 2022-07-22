package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

/**
 * Checkout config creating callback interface definition.
 */
internal interface VGSCheckoutConfigInitCallback {

    /**
     * Invoked when checkout config creation successfully complete.
     *
     * @param config instance of checkout config.
     */
    fun onSuccess()

    /**
     * Invoked when checkout config creation failed.
     *
     * @param exception explains why checkout config creation failed.
     */
    fun onFailure(exception: VGSCheckoutException)
}