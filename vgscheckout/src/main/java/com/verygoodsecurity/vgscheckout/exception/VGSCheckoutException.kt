package com.verygoodsecurity.vgscheckout.exception

abstract class VGSCheckoutException internal constructor(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)