package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.inetPermissionsGranted
import com.verygoodsecurity.vgscheckout.collect.util.extension.isConnectionAvailable
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetPermissionException
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient

internal abstract class Command<P : Command.Params, R : Command.Result> constructor(
    private val context: Context
) : VGSCheckoutCancellable {

    protected val client = HttpClient.create(false)

    /**
     * Execute command.
     */
    fun execute(params: P, onResult: (result: R) -> Unit) {
        when {
            !context.inetPermissionsGranted() -> onResult.invoke(map(NoInternetPermissionException()))
            !context.isConnectionAvailable() -> onResult.invoke(map(NoInternetConnectionException()))
            else -> run(params) { onResult.invoke(it) }
        }
    }

    protected abstract fun run(params: P, onResult: (R) -> Unit)

    protected abstract fun map(exception: VGSCheckoutException): R

    override fun cancel() {
        client.cancelAll()
    }

    internal abstract class Params

    internal abstract class Result
}