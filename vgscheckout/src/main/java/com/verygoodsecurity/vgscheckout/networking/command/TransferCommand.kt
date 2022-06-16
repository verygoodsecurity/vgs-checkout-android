package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.command.core.Command

internal class TransferCommand constructor(
    context: Context,
    params: Params
) : Command<TransferCommand.Params, TransferCommand.Result>(context, params) {

    override fun run(params: Params, onResult: (Result) -> Unit) {
        client.enqueue(createRequest(params)) {
            onResult.invoke(
                Result(
                    it.isSuccessful,
                    it.code,
                    it.message,
                    it.body,
                    it.latency
                )
            )
        }
    }

    override fun map(params: Params, exception: VGSCheckoutException): Result =
        Result(
            false,
            exception.code,
            exception.message,
            null,
            0
        )

    private fun createRequest(params: Params) = HttpRequest(
        params.url concatWithSlash PATH,
        generatePayload(params),
        mapOf(
            AUTHORIZATION_HEADER_KEY to String.format(
                AUTHORIZATION_HEADER_VALUE,
                params.accessToken
            )
        ),
        HttpMethod.POST
    )

    private fun generatePayload(params: Params) =
        mutableMapOf(
            JSON_KEY_ORDER_ID to params.orderId,
            JSON_KEY_SOURCE to params.source
        ).toJSON().toString()

    companion object {

        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val AUTHORIZATION_HEADER_VALUE = "Bearer %s"

        private const val PATH = "transfers"

        private const val JSON_KEY_ORDER_ID = "order_id"
        private const val JSON_KEY_SOURCE = "source"
        private const val JSON_KEY_CURRENCY = "currency"
    }

    internal data class Params(
        val url: String,
        val orderId: String,
        val source: String,
        val accessToken: String
    ) : Command.Params()

    data class Result constructor(
        val isSuccessful: Boolean,
        val code: Int,
        val message: String?,
        val body: String?,
        val latency: Long
    ) : Command.Result()
}