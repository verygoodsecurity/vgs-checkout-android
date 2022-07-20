package com.verygoodsecurity.vgscheckout.exception

/**
 * Throws to indicate that something get wrong. Check error code explanation in our doc TODO: Add link
 */
open class VGSCheckoutException internal constructor(
    val code: Int,
    message: String?,
    cause: Throwable?,
) : Exception(message, cause) {

    override fun toString(): String {
        return "${this::class.java}\ncode = $code\nmessage = $message\ncause = $cause"
    }
}