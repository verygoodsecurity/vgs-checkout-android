package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 2001
private const val MESSAGE = "Access token contains restricted role [%s]."

/**
 * Throws to indicate that access token contains restricted access roles.
 */
internal class JWTRestrictedRoleException internal constructor(
    role: String,
) : VGSCheckoutException(CODE, String.format(MESSAGE, role), null)