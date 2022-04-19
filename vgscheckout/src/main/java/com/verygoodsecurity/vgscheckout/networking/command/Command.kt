package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasAccessNetworkStatePermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasInternetPermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.isConnectionAvailable
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetPermissionException
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient

internal abstract class Command<P : Command.Params> constructor(
    private val context: Context
) : Cancellable {

    protected val client = HttpClient.create(false)

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Execute command. Result always returned on main thread.
     */
    fun execute(params: P, onResult: (Result) -> Unit) {
        when {
            !context.hasAccessNetworkStatePermission() || !context.hasInternetPermission() -> {
                post(onResult, Result.create(NoInternetPermissionException()))
            }
            !context.isConnectionAvailable() -> {
                post(onResult, Result.create(NoInternetConnectionException()))
            }
            else -> run(params) { post(onResult, it) }
        }
    }

    protected abstract fun run(params: P, onResult: (Result) -> Unit)

    override fun cancel() {
        client.cancelAll()
    }

    private fun post(onResult: (Result) -> Unit, result: Result) {
        handler.post { onResult.invoke(result) }
    }

    internal abstract class Params(
        baseUrl: String,
        path: String
    ) {

        val url = baseUrl concatWithSlash path
    }

    internal data class Result(
        val isSuccessful: Boolean,
        val code: Int,
        val message: String?,
        val body: String?,
        val latency: Long
    ) {

        fun isNoInternetConnectionCode() = code == NoInternetConnectionException.CODE

        companion object {

            fun create(exception: VGSCheckoutException) = Result(
                false,
                exception.code,
                exception.message,
                null,
                0
            )
        }
    }
}