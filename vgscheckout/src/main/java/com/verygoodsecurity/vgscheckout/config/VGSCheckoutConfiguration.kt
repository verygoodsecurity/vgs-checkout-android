package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutConfiguration @JvmOverloads constructor(
    override val vaultID: String,
    override val environment: String = DEFAULT_ENVIRONMENT,
    override val routeConfig: VGSCheckoutRouteConfiguration = VGSCheckoutRouteConfiguration(),
    override val formConfig: VGSCheckoutFormConfiguration = VGSCheckoutFormConfiguration(),
) : CheckoutConfiguration()