package com.verygoodsecurity.vgscheckout.collect.core.model.network

import com.verygoodsecurity.vgscheckout.collect.core.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.collect.core.networking.client.HttpBodyFormat

private const val DEFAULT_REQUEST_TIMEOUT = 60_000L

internal data class HttpRequest(
    var url: String,
    val payload: Any,
    val headers: Map<String, String> = emptyMap(),
    val method: HttpMethod = HttpMethod.POST,
    val format: HttpBodyFormat = HttpBodyFormat.JSON,
    val timeoutInterval: Long = DEFAULT_REQUEST_TIMEOUT
)