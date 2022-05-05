package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.analytic.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import java.util.*

/**
 * Base class of checkout config.
 *
 * @property id unique organization vault id.
 */
abstract class CheckoutConfig internal constructor(internal val id: String) : Parcelable {

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
     * If true, checkout form will allow to make screenshots.
     */
    abstract val isScreenshotsAllowed: Boolean

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(id, environment.value, UUID.randomUUID().toString())
    }
}