package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1481
private const val MESSAGE = "Permission denied (missing INTERNET permission?)"

/**
 * Throws to indicate that internet permission doesn't granted.
 */
class NoInternetPermissionException : VGSCheckoutException(CODE, MESSAGE, null)