package com.verygoodsecurity.orchestration.example.network.util

internal fun Number.isSuccessHttpCode() = this in 200..299
