package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1483
private const val MESSAGE = "Network request timeout error."

/**
 * Throws to indicate that network request failed due to timeout.
 */
class TimeoutNetworkingException : VGSCheckoutException(CODE, MESSAGE, null)