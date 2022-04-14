package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toNetworkRequest
import com.verygoodsecurity.vgscheckout.collect.util.extension.deepMerge
import com.verygoodsecurity.vgscheckout.collect.util.extension.toFlatMap
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig

internal fun CheckoutRouteConfig.toSaveCardNetworkRequest(
    baseURL: String,
    data: MutableCollection<Pair<String, String>>
): HttpRequest {
    return VGSRequest.VGSRequestBuilder()
        .setPath(path)
        .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
        .setCustomHeader(requestOptions.extraHeaders)
        .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
        .build().toNetworkRequest(baseURL, mergeData(data))
}

private fun CheckoutRouteConfig.mergeData(
    data: MutableCollection<Pair<String, String>>
): Map<String, Any> {
    with(requestOptions.extraData.toMutableMap()) {
        return deepMerge(
            data.toFlatMap(
                requestOptions.mergePolicy.toCollectMergePolicy().isArraysAllowed()
            ).structuredData,
            requestOptions.mergePolicy.toCollectMergePolicy().getMergeArraysPolicy()
        )
    }
}