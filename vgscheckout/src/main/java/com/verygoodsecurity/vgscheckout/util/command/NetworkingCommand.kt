package com.verygoodsecurity.vgscheckout.util.command

import android.os.Handler
import android.os.Looper
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient

internal abstract class NetworkingCommand<P, R : Result<*>> {

    protected val client = ApiClient.create(false)

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Execute command. Result always returned in main thread.
     */
    fun execute(params: P, onResult: (R) -> Unit): VGSCheckoutCancellable = run(params) {
        handler.post {
            onResult.invoke(it)
        }
    }

    protected abstract fun run(params: P, onResult: (R) -> Unit): VGSCheckoutCancellable

    protected companion object {

        const val DEFAULT_REQUEST_TIMEOUT = 60_000L
    }
}