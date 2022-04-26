package com.verygoodsecurity.vgscheckout.networking.client

private const val DEFAULT_REQUEST_TIMEOUT = 60_000L

internal data class HttpRequest(
    var url: String,
    val payload: Any?,
    val headers: Map<String, String> = emptyMap(),
    val method: HttpMethod = HttpMethod.POST,
    val format: HttpBodyFormat = HttpBodyFormat.JSON,
    val timeoutInterval: Long = DEFAULT_REQUEST_TIMEOUT
)