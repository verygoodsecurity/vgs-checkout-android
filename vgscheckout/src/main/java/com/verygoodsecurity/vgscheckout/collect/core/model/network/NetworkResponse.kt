package com.verygoodsecurity.vgscheckout.collect.core.model.network

import android.content.Context

internal data class NetworkResponse(
    val isSuccessful: Boolean = false,
    val body: String? = null,
    val code: Int = -1,
    val message: String? = null,
    val error: VGSError? = null,
    val latency: Long = 0
)

internal fun NetworkResponse.toVGSResponse(
    context: Context? = null
): VGSResponse {
    return when {
        this.isSuccessful -> VGSResponse.SuccessResponse(
            successCode = this.code,
            rawResponse = this.body
        )
        this.error != null -> VGSResponse.ErrorResponse(
            localizeMessage = context?.getString(error.messageResId) ?: "",
            errorCode = error.code,
            rawResponse = this.body
        )
        else -> VGSResponse.ErrorResponse(
            localizeMessage = this.message ?: "",
            errorCode = this.code,
            rawResponse = this.body
        )
    }
}