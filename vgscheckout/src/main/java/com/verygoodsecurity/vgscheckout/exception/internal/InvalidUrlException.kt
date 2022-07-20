package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1480
private const val MESSAGE = "URL is not valid."

/**
 * Throws to indicate that url is invalid.
 */
internal class InvalidUrlException : VGSCheckoutException(CODE, MESSAGE, null)