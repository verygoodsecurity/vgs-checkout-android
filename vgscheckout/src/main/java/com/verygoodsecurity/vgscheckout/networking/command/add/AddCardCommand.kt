package com.verygoodsecurity.vgscheckout.networking.command.add

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.collect.util.extension.deepMerge
import com.verygoodsecurity.vgscheckout.collect.util.extension.toFlatMap
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.command.Command
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.util.extension.toInternal

internal class AddCardCommand constructor(context: Context) :
    Command<AddCardCommand.Params, VGSCheckoutAddCardResponse>(context) {

    override fun run(
        params: Params,
        onResult: (VGSCheckoutAddCardResponse) -> Unit
    ) {
        client.enqueue(createRequest(params)) {
            onResult.invoke(
                VGSCheckoutAddCardResponse(
                    it.isSuccessful,
                    it.code,
                    it.body,
                    it.message,
                    it.latency
                )
            )
        }
    }

    private fun createRequest(params: Params): HttpRequest {
        val url = params.baseUrl concatWithSlash params.config.path
        val payload = generatePayload(params)
        val headers = params.config.requestOptions.extraHeaders
        val method = params.config.requestOptions.httpMethod.toInternal()
        return HttpRequest(url, payload, headers, method)
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
        val baseUrl: String,
        val config: CheckoutRouteConfig,
        val data: MutableCollection<Pair<String, String>>,
    )
}