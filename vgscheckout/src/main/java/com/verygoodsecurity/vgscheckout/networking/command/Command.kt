package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasAccessNetworkStatePermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasInternetPermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.isConnectionAvailable
import com.verygoodsecurity.vgscheckout.model.response.core.VGSCheckoutResponse
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient

internal abstract class Command<P, R : VGSCheckoutResponse> constructor(private val context: Context) :
    Cancellable {

    protected val client = HttpClient.create(false)

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Execute command. Result always returned in main thread.
     */
    fun execute(params: P, onResult: (R) -> Unit) {
        when {
            !context.hasInternetPermission() -> TODO("Handle hasInternetPermission")
            !context.hasAccessNetworkStatePermission() -> TODO("Handle hasAccessNetworkStatePermission")
            !context.isConnectionAvailable() -> TODO("Handle isConnectionAvailable")
            else -> {
                run(params) {
                    handler.post {
                        onResult.invoke(it)
                    }
                }
            }
        }
    }

    protected abstract fun run(params: P, onResult: (R) -> Unit)

    override fun cancel() {
        client.cancelAll()
    }
}