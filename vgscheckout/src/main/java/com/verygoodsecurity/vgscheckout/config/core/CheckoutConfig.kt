package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import java.util.*

/**
 * Base class for checkout config.
 */
abstract class CheckoutConfig internal constructor() : Parcelable {

    /**
     * Unique organization vault id.
     */
    abstract val vaultID: String

    /**
     * Type of vault.
     *
     * @see [VGSCheckoutEnvironment].
     */
    abstract val environment: VGSCheckoutEnvironment

    /**
     *  Networking configuration, like http method, request headers etc.
     */
    abstract val routeConfig: CheckoutRouteConfig

    /**
     * UI configuration.
     */
    abstract val formConfig: CheckoutFormConfig

    /**
     * If true, checkout will send analytics events that helps to debug issues if any occurs.
     */
    abstract val isAnalyticsEnabled: Boolean

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(vaultID, environment.value, UUID.randomUUID().toString()).apply {
            isEnabled = isAnalyticsEnabled
        }
    }
}