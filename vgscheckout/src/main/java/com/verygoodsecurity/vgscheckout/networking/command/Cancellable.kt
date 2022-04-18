package com.verygoodsecurity.vgscheckout.networking.command

/**
 * A {@code VGSCancellable} is a action that can be canceled.
 */
interface Cancellable {

    /**
     * Cancel action.
     */
    fun cancel()
}