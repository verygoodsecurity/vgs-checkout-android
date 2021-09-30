package com.verygoodsecurity.vgscheckout.collect.core.model.network

import android.content.Context

internal data class NetworkResponse(
    val isSuccessful: Boolean = false,
    val code: Int = -1,
    val body: String? = null,
    val message: String? = null,
    val error: VGSError? = null,
    val latency: Long = 0
)

internal fun NetworkResponse.toVGSResponse(context: Context? = null): VGSResponse {
    return when {
        this.isSuccessful -> VGSResponse.SuccessResponse(
            code = this.code,
            body = this.body,
            latency = this.latency
        )
        this.error != null -> VGSResponse.ErrorResponse(
            message = context?.getString(error.messageResId) ?: "",
            code = error.code,
            body = this.body,
            latency = this.latency
        )
        else -> VGSResponse.ErrorResponse(
            message = this.message ?: "",
            code = this.code,
            body = this.body,
            latency = this.latency
        )
    }
}