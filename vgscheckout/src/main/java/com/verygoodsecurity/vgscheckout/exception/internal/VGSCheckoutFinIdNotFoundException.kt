package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1485
private const val MESSAGE =
    "Request to multiplexing succeed, cannot find financial instrument id in response."

/**
 * Throws to indicate that response doesn't contains fin in or it can't be parsed.
 */
internal class VGSCheckoutFinIdNotFoundException constructor(cause: Throwable?) :
    VGSCheckoutException(CODE, MESSAGE, cause)