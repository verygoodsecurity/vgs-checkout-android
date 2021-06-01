package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig
import kotlinx.parcelize.Parcelize

@Suppress("PARCELABLE_PRIMARY_CONSTRUCTOR_IS_EMPTY")
@Parcelize
class VGSMultiplexingRequestConfig private constructor() : RequestConfig() {

    class Builder {

        fun build() = VGSMultiplexingRequestConfig()
    }
}