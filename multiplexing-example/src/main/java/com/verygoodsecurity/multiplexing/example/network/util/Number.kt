package com.verygoodsecurity.multiplexing.example.network.util

internal fun Number.isSuccessHttpCode() = this in 200..299
