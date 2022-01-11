package com.verygoodsecurity.vgscheckout.util.command

import android.os.Handler
import android.os.Looper

internal abstract class AsynchronousCommand<P, R : Result<*>> {

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Execute command. Result always returned in main thread.
     */
    fun execute(parameter: P, onResult: (R) -> Unit): VGSCheckoutCancellable = run(parameter) {
        handler.post {
            onResult.invoke(it)
        }
    }

    protected abstract fun run(parameter: P, onResult: (R) -> Unit): VGSCheckoutCancellable
}