package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.command.core.Command

internal class DeleteCreditCardCommand constructor(context: Context) :
    Command<DeleteCreditCardCommand.Params, DeleteCreditCardCommand.Result>(context) {

    override fun run(params: Params, onResult: (Result) -> Unit) {
        client.enqueue(createRequest(params)) {
            onResult.invoke(
                Result(params.id, it.isSuccessful, it.code, it.message, it.body, it.latency)
            )
        }
    }

    override fun map(params: Params, exception: VGSCheckoutException) = Result(
        params.id,
        false,
        exception.code,
        exception.message,
        null,
        0
    )

    private fun createRequest(params: Params) = HttpRequest(
        (params.url concatWithSlash params.path) concatWithSlash params.id,
        null,
        mapOf(
            AUTHORIZATION_HEADER_KEY to String.format(
                AUTHORIZATION_HEADER_VALUE,
                params.accessToken
            )
        ),
        HttpMethod.DELETE
    )

    companion object {

        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val AUTHORIZATION_HEADER_VALUE = "Bearer %s"
    }

    data class Params constructor(
        val url: String,
        val path: String,
        val accessToken: String,
        val id: String
    ) : Command.Params()

    data class Result constructor(
        val id: String,
        val isSuccessful: Boolean,
        val code: Int,
        val message: String?,
        val body: String?,
        val latency: Long
    ) : Command.Result()
}