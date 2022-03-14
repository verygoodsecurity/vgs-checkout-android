package com.verygoodsecurity.vgscheckout.collect.core.model.network

internal data class NetworkResponse(
    val isSuccessful: Boolean = false,
    val code: Int = -1,
    val body: String? = null,
    val message: String? = null,
    val latency: Long = 0
) {

    companion object {

        fun create(error: VGSError) = NetworkResponse(
            code = error.code,
            message = error.message
        )
    }
}