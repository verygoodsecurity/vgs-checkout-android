package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.RouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSVaultRequestConfig
import com.verygoodsecurity.vgscollect.core.Environment
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSVaultRouteConfig private constructor(
    override val environment: Environment,
    override val requestConfig: VGSVaultRequestConfig
) : RouteConfig<VGSVaultRequestConfig>() {

    class Builder {

        private var environment = Environment.SANDBOX
        private var requestConfig = VGSVaultRequestConfig.Builder().build()

        fun setEnvironment(environment: Environment) = this.apply {
            this.environment = environment
        }

        fun setRequestConfig(config: VGSVaultRequestConfig) = this.apply {
            this.requestConfig = config
        }

        fun build() = VGSVaultRouteConfig(environment, requestConfig)
    }
}