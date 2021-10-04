package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCustomConfiguration @JvmOverloads constructor(
    override val vaultID: String,
    override val environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
    override val routeConfig: VGSCheckoutRouteConfiguration = VGSCheckoutRouteConfiguration(),
    override val formConfig: VGSCheckoutFormConfiguration = VGSCheckoutFormConfiguration(),
    override val isAnalyticsEnabled: Boolean = true
) : CheckoutConfiguration()