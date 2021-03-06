package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 2003
private const val MESSAGE = "Cannot fetch order id info."

/**
 * Throws to indicate that payment info can't be parsed.
 */
internal class PaymentInfoParseException constructor(cause: Throwable?) :
    VGSCheckoutException(CODE, MESSAGE, cause)