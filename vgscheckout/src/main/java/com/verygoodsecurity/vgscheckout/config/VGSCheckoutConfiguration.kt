package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutConfiguration private constructor(
    override val vaultID: String,
    override val environment: String,
    val routeConfig: VGSCheckoutRouteConfiguration,
    val formConfig: VGSCheckoutFormConfiguration,
) : CheckoutConfiguration() {

    class Builder constructor(
        private val vaultID: String,
        private val environment: String = DEFAULT_ENVIRONMENT,
    ) {

        private var routeConfig = VGSCheckoutRouteConfiguration.Builder().build()
        private var formConfig = VGSCheckoutFormConfiguration.Builder().build()

        fun setRouteConfig(config: VGSCheckoutRouteConfiguration) = this.also {
            this.routeConfig = config
        }

        fun setFormConfig(config: VGSCheckoutFormConfiguration) = this.also {
            this.formConfig = config
        }

        fun build() = VGSCheckoutConfiguration(vaultID, environment, routeConfig, formConfig)
    }
}