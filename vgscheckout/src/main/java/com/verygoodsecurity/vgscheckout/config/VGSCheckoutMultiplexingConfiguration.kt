package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingConfiguration private constructor(
    override val vaultID: String,
    override val environment: String,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutMultiplexingFormConfiguration
) : CheckoutConfiguration() {

    class Builder constructor(
        private val vaultID: String,
        private val environment: String = DEFAULT_ENVIRONMENT
    ) {

        private var formConfig = VGSCheckoutMultiplexingFormConfiguration.Builder().build()

        fun setFormConfig(config: VGSCheckoutMultiplexingFormConfiguration) = this.also {
            this.formConfig = config
        }

        fun build() = VGSCheckoutMultiplexingConfiguration(
            vaultID,
            environment,
            VGSCheckoutRouteConfiguration.Builder().build(),
            formConfig
        )
    }
}