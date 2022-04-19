package com.verygoodsecurity.vgscheckout.networking.command.add

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.deepMerge
import com.verygoodsecurity.vgscheckout.collect.util.extension.toFlatMap
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.command.Command
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.util.extension.toCommandResult
import com.verygoodsecurity.vgscheckout.util.extension.toInternal

internal class AddCardCommand constructor(context: Context) :
    Command<AddCardCommand.AddCardParams>(context) {

    override fun run(
        params: AddCardParams,
        onResult: (Result) -> Unit
    ) {
        client.enqueue(createRequest(params)) { onResult.invoke(it.toCommandResult()) }
    }

    private fun createRequest(params: AddCardParams): HttpRequest {
        val payload = generatePayload(params)
        val headers = params.config.requestOptions.extraHeaders
        val method = params.config.requestOptions.httpMethod.toInternal()
        return HttpRequest(params.url, payload, headers, method)
    }

    private fun generatePayload(params: AddCardParams): Map<String, Any> {
        val collectMergePolicy = params.config.requestOptions.mergePolicy.toCollectMergePolicy()
        val isArraysAllowed = collectMergePolicy.isArraysAllowed()
        val arrayMergePolicy = collectMergePolicy.getMergeArraysPolicy()
        val structuredData = params.data.toFlatMap(isArraysAllowed).structuredData
        val extraData = params.config.requestOptions.extraData.toMutableMap()
        return extraData.deepMerge(structuredData, arrayMergePolicy)
    }

    internal class AddCardParams(
        baseUrl: String,
        path: String,
        val config: CheckoutRouteConfig,
        val data: MutableCollection<Pair<String, String>>,
    ) : Command.Params(baseUrl, path)
}