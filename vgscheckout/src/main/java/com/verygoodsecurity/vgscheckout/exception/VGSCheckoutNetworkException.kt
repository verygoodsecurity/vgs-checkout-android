package com.verygoodsecurity.vgscheckout.exception

class VGSCheckoutNetworkException constructor(
    message: String? = null,
    cause: Throwable? = null,
    val code: Int? = null,
    val body: String? = null,
) : VGSCheckoutException(message, cause) {

    override fun toString(): String {
        return "${this::class.java}\ncode = $code\nmessage = $message\nbody = $body"
    }
}