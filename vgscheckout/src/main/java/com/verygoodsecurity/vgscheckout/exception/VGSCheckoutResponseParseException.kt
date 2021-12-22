package com.verygoodsecurity.vgscheckout.exception

class VGSCheckoutResponseParseException constructor(
    message: String,
    cause: Throwable? = null
) : VGSCheckoutException(message, cause)