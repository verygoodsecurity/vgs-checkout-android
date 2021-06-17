package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingConfiguration private constructor(
    override val vaultID: String,
    override val environment: String
) : CheckoutConfiguration() {

    class Builder constructor(
        private val vaultID: String,
        private val environment: String = DEFAULT_ENVIRONMENT
    ) {

        fun build() = VGSCheckoutMultiplexingConfiguration(vaultID, environment)
    }
}