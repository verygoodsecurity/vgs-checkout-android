package com.verygoodsecurity.vgscheckout.networking.client

internal enum class HttpBodyFormat constructor(val value: String) {
    JSON("application/json"),
    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded")
}