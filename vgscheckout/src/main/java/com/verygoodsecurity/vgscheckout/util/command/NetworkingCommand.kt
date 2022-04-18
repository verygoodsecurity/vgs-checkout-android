package com.verygoodsecurity.vgscheckout.util.command

import android.os.Handler
import android.os.Looper
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient

internal abstract class NetworkingCommand<P, R : Result<*>> : VGSCheckoutCancellable {

    protected val client = HttpClient.create(false)

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

    override fun cancel() {
        client.cancelAll()
    }
}