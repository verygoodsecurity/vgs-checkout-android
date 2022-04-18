package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse

internal fun HttpResponse.toVGSResponse(): VGSResponse {
    return when {
        this.isSuccessful -> VGSResponse.SuccessResponse(
            code = this.code,
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