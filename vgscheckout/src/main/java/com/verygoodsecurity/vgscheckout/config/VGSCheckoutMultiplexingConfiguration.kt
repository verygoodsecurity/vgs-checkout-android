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

    @JvmOverloads
    constructor(
        vaultID: String,
        environment: String = DEFAULT_ENVIRONMENT,
        formConfig: VGSCheckoutMultiplexingFormConfiguration = VGSCheckoutMultiplexingFormConfiguration()
    ) : this(
        vaultID,
        environment,
        VGSCheckoutRouteConfiguration(),
        formConfig
    )
}