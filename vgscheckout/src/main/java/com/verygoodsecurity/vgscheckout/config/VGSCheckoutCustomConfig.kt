package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import kotlinx.parcelize.Parcelize

/**
 * Holds configuration for vault payment processing with custom configuration.
 *
 * @param vaultID unique organization vault id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
 * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues
 * if any occurs. Default value is true.
 */
@Parcelize
class VGSCheckoutCustomConfig @JvmOverloads constructor(
    val vaultID: String,
    override val environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
    override val routeConfig: VGSCheckoutCustomRouteConfig = VGSCheckoutCustomRouteConfig(),
    override val formConfig: VGSCheckoutCustomFormConfig = VGSCheckoutCustomFormConfig(),
    override val isScreenshotsAllowed: Boolean = false,
    override val isAnalyticsEnabled: Boolean = true,
) : CheckoutConfig(vaultID)