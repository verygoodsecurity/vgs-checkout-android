package com.verygoodsecurity.vgscheckout.exception

/**
 * Throws to indicate  that network request if failed.
 */
class VGSCheckoutNetworkException internal constructor(
    code: Int,
    message: String?,
    cause: Throwable? = null,
    val body: String? = null,
) : VGSCheckoutException(code, message, cause) {

    override fun toString(): String {
        return "${this::class.java}\ncode = $code\nmessage = $message\nbody = $body"
    }
}