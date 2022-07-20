package com.verygoodsecurity.vgscheckout.networking.client

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

internal data class HttpResponse(
    val isSuccessful: Boolean,
    val code: Int,
    val body: String? = null,
    val message: String? = null,
    val latency: Long = 0
) {

    companion object {

        fun create(exception: VGSCheckoutException) = HttpResponse(
            isSuccessful = false,
            code = exception.code,
            message = exception.message
        )
    }
}