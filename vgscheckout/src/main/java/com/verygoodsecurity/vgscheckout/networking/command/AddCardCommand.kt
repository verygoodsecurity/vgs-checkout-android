package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.collect.util.extension.deepMerge
import com.verygoodsecurity.vgscheckout.collect.util.extension.toFlatMap
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.command.core.Command
import com.verygoodsecurity.vgscheckout.util.extension.toAddCardResult
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.util.extension.toInternal

internal class AddCardCommand constructor(context: Context) :
    Command<AddCardCommand.Params, AddCardCommand.Result>(context) {

    override fun run(
        params: Params,
        onResult: (Result) -> Unit
    ) {
        client.enqueue(createRequest(params)) { onResult.invoke(it.toAddCardResult()) }
    }

    override fun map(params: Params, exception: VGSCheckoutException) = Result(
        false,
        exception.code,
        exception.message,
        null,
        0
    )

    private fun createRequest(params: Params): HttpRequest {
        val payload = generatePayload(params).toJSON().toString()
        val headers = params.config.requestOptions.extraHeaders
        val method = params.config.requestOptions.httpMethod.toInternal()
        return HttpRequest(params.url concatWithSlash params.path, payload, headers, method)
    }

    private fun generatePayload(params: Params): Map<String, Any> {
        val collectMergePolicy = params.config.requestOptions.mergePolicy.toCollectMergePolicy()
        val isArraysAllowed = collectMergePolicy.isArraysAllowed()
        val arrayMergePolicy = collectMergePolicy.getMergeArraysPolicy()
        val structuredData = params.data.toFlatMap(isArraysAllowed).structuredData
        val extraData = params.config.requestOptions.extraData.toMutableMap()
        return extraData.deepMerge(structuredData, arrayMergePolicy)
    }

    internal data class Params(
        val url: String,
        val path: String,
        val config: CheckoutRouteConfig,
        val data: MutableCollection<Pair<String, String>>,
    ) : Command.Params()

    internal data class Result(
        val isSuccessful: Boolean,
        val code: Int,
        val message: String?,
        val body: String?,
        val latency: Long
    ) : Command.Result()
}