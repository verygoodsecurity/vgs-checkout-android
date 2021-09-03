package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingConfiguration private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: String,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutMultiplexingFormConfiguration
) : CheckoutConfiguration() {

    /**
     * @throws IllegalArgumentException if token is not valid.
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    constructor(
        vaultID: String,
        token: String,
        environment: String = DEFAULT_ENVIRONMENT
    ) : this(
        token,
        vaultID,
        environment,
        VGSCheckoutRouteConfiguration(),
        VGSCheckoutMultiplexingFormConfiguration()
    )
}