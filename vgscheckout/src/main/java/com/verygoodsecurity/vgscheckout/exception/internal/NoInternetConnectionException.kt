package com.verygoodsecurity.vgscheckout.exception.internal

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

/**
 * Throws to indicate that no internet connection.
 */
internal class NoInternetConnectionException : VGSCheckoutException(CODE, MESSAGE, null) {

    companion object {

        internal const val CODE = 1482
        private const val MESSAGE = "No internet connection."
    }
}