package com.verygoodsecurity.vgscheckout.collect.core.api.client.extension

internal fun Int.isCodeSuccessful(): Boolean {
    return this in 200..299
}

internal fun Int.isHttpStatusCode(): Boolean {
    return this in 200..999
}