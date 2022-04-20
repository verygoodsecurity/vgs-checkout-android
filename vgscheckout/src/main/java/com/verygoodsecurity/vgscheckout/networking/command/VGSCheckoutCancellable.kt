package com.verygoodsecurity.vgscheckout.networking.command

/**
 * A {@code VGSCancellable} is a action that can be canceled.
 */
interface VGSCheckoutCancellable {

    /**
     * Cancel action.
     */
    fun cancel()
}