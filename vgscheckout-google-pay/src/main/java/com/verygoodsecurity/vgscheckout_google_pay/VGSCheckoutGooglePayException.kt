@file:Suppress("MemberVisibilityCanBePrivate")

package com.verygoodsecurity.vgscheckout_google_pay

import com.google.android.gms.common.api.Status

// TODO: Make Exception is more reusable
class VGSCheckoutGooglePayException internal constructor(
    val statusCode: Int,
    val statusMessage: String,
) : Exception() {

    override val message: String = statusMessage

    override fun getLocalizedMessage(): String = "Code = $statusCode, message = $statusMessage"

    companion object {

        private const val DEFAULT_STATUS_CODE = -1
        private const val DEFAULT_STATUS_MESSAGE = "<GOOGLE_PAY_ERROR_MESSAGE>"

        fun create(status: Status?) = VGSCheckoutGooglePayException(
            status?.statusCode ?: DEFAULT_STATUS_CODE,
            status?.statusMessage ?: DEFAULT_STATUS_MESSAGE,
        )
    }
}