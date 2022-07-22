package com.verygoodsecurity.vgscheckout.networking.command.core

import androidx.annotation.CallSuper

/**
 * A {@code VGSCancellable} is a action that can be canceled.
 */
abstract class VGSCheckoutCancellable {

    internal var isCancelled: Boolean = false
        private set

    /**
     * Cancel action.
     */
    @CallSuper
    open fun cancel() {
        isCancelled = true
    }
}