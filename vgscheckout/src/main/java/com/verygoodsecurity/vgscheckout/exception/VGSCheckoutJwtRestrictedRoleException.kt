package com.verygoodsecurity.vgscheckout.exception

private const val MESSAGE = "JWT token contains restricted role [%s]."

/**
 * Throws to indicate that JWT token contains restricted access roles.
 */
class VGSCheckoutJwtRestrictedRoleException internal constructor(
    role: String,
) : VGSCheckoutException(String.format(MESSAGE, role))