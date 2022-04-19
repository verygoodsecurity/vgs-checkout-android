package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 0
private const val MESSAGE = "Network request failed, cause: [%s]."

/**
 * Throws to indicate that network request failed due to unexpected exception.
 */
class UnexpectedNetworkingException constructor(
    override val cause: Throwable
) : VGSCheckoutException(CODE, String.format(MESSAGE, cause::class.java.simpleName), null)