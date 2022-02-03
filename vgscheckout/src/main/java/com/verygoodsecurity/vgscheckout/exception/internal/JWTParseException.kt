package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 2000
private const val MESSAGE = "Can't parse invalid access token."

/**
 * Throws to indicate that access token can't be parsed.
 */
internal class JWTParseException : VGSCheckoutException(CODE, MESSAGE, null)