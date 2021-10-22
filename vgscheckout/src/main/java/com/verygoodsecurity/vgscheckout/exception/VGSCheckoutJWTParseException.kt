package com.verygoodsecurity.vgscheckout.exception

private const val MESSAGE = "Can't parse invalid JWT token."

/**
 * Throws to indicate that JWT token can't be parsed.
 */
class VGSCheckoutJWTParseException internal constructor() : VGSCheckoutException(MESSAGE)