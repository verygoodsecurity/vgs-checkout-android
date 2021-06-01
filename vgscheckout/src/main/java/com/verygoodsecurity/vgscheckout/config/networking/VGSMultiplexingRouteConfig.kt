package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.RouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSMultiplexingRequestConfig
import com.verygoodsecurity.vgscollect.core.Environment
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSMultiplexingRouteConfig private constructor(
    override val environment: Environment,
    override val requestConfig: VGSMultiplexingRequestConfig
) : RouteConfig<VGSMultiplexingRequestConfig>() {

    class Builder {

        private val environment = Environment.SANDBOX
        private val requestConfig = VGSMultiplexingRequestConfig.Builder().build()

        fun build() = VGSMultiplexingRouteConfig(environment, requestConfig)
    }
}