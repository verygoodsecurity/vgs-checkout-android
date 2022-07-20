package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

private const val CODE = 1004
private const val MESSAGE = "Field name is not set."

/**
 * Throws to indicate that field name doesn't set for input.
 */
class FieldNameNotSetException : VGSCheckoutException(CODE, MESSAGE, null)