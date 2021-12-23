package com.verygoodsecurity.vgscheckout.exception

private const val MESSAGE = "Can't parse invalid access token."

/**
 * Throws to indicate that access token can't be parsed.
 */
class VGSCheckoutJWTParseException internal constructor() : VGSCheckoutException(MESSAGE, null)