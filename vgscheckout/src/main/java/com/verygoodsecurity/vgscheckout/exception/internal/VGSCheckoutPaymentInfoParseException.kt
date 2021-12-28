package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1476
private const val MESSAGE = "Cannot fetch order id info."

/**
 * Throws to indicate that payment info can't be parsed.
 */
internal class VGSCheckoutPaymentInfoParseException constructor(cause: Throwable?) :
    VGSCheckoutException(CODE, MESSAGE, cause)