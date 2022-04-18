package com.verygoodsecurity.vgscheckout.collect.core.networking.client

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError

internal data class HttpResponse(
    val isSuccessful: Boolean = false,
    val code: Int = -1,
    val body: String? = null,
    val message: String? = null,
    val latency: Long = 0
) {

    companion object {

        fun create(error: VGSError) = HttpResponse(
            code = error.code,
            message = error.message
        )
    }
}