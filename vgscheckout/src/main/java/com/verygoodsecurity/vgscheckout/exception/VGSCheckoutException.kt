package com.verygoodsecurity.vgscheckout.exception

abstract class VGSCheckoutException internal constructor(
    message: String?,
    cause: Throwable?,
) : Exception(message, cause)