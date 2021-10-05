package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCustomConfig @JvmOverloads constructor(
    override val vaultID: String,
    override val environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
    override val routeConfig: VGSCheckoutCustomRouteConfig = VGSCheckoutCustomRouteConfig(),
    override val formConfig: VGSCheckoutCustomFormConfig = VGSCheckoutCustomFormConfig(),
    override val isAnalyticsEnabled: Boolean = true
) : CheckoutConfig()