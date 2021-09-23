package com.verygoodsecurity.vgscheckout.collect.core.api.client.extension

import okhttp3.Response

internal fun Int.isCodeSuccessful(): Boolean {
    return this in 200..299
}

internal fun Int.isHttpStatusCode(): Boolean {
    return this in 200..999
}

internal fun Response.latency() = this.receivedResponseAtMillis - this.sentRequestAtMillis