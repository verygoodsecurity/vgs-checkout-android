package com.verygoodsecurity.vgscheckout.networking.command.core

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.verygoodsecurity.vgscheckout.collect.util.extension.inetPermissionsGranted
import com.verygoodsecurity.vgscheckout.collect.util.extension.isConnectionAvailable
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetPermissionException
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient

internal abstract class Command<P : Command.Params, R : Command.Result> constructor(
    private val context: Context,
    private val params: P
) : VGSCheckoutCancellable() {

    protected val client = HttpClient.create(false)

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Execute command. Result always returned on main thread.
     */
    fun execute(onResult: (result: R) -> Unit) {
        when {
            !context.inetPermissionsGranted() -> post(
                onResult,
                map(params, NoInternetPermissionException())
            )
            !context.isConnectionAvailable() -> post(
                onResult,
                map(params, NoInternetConnectionException())
            )
            else -> run(params) { post(onResult, it) }
        }
    }

    protected abstract fun run(params: P, onResult: (R) -> Unit)

    protected abstract fun map(params: P, exception: VGSCheckoutException): R

    override fun cancel() {
        super.cancel()
        client.cancelAll()
    }

    private fun post(onResult: (R) -> Unit, result: R) {
        handler.post { onResult.invoke(result) }
    }

    internal abstract class Params

    internal abstract class Result
}