package com.verygoodsecurity.vgscheckout.collect.core.api

internal enum class VGSHttpBodyFormat {
    PLAIN_TEXT,
    JSON,
    X_WWW_FORM_URLENCODED
}

internal fun VGSHttpBodyFormat.toContentType() = when (this) {
    VGSHttpBodyFormat.JSON -> "application/json"
    VGSHttpBodyFormat.PLAIN_TEXT -> "text/plain"
    VGSHttpBodyFormat.X_WWW_FORM_URLENCODED -> "application/x-www-form-urlencoded"
}