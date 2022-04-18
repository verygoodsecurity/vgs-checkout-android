package com.verygoodsecurity.vgscheckout.collect.core.api

internal enum class HttpBodyFormat {
    PLAIN_TEXT,
    JSON,
    X_WWW_FORM_URLENCODED
}

internal fun HttpBodyFormat.toContentType() = when (this) {
    HttpBodyFormat.JSON -> "application/json"
    HttpBodyFormat.PLAIN_TEXT -> "text/plain"
    HttpBodyFormat.X_WWW_FORM_URLENCODED -> "application/x-www-form-urlencoded"
}