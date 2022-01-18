package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 2004
private const val MESSAGE = "Failed to retrieve checkout result."

/**
 * Throws to indicate that checkout result can't be parsed.
 */
internal class VGSCheckoutResultParseException : VGSCheckoutException(CODE, MESSAGE, null)