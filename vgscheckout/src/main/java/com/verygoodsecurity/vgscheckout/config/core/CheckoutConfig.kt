package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.analytic.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import java.util.*

/**
 * Base class of checkout config.
 *
 * @property id unique organization vault id.
 */
abstract class CheckoutConfig : Parcelable {

    internal abstract val baseUrl: String

    internal abstract val routeId: String

    internal abstract val id: String

    /**
     * Type of vault.
     *
     * @see [VGSCheckoutEnvironment].
     */
    abstract val environment: VGSCheckoutEnvironment

    /**
     *  Networking configuration, like http method, request headers etc.
     */
    abstract val routeConfig: VGSCheckoutRouteConfig

    /**
     * UI configuration.
     */
    abstract val formConfig: VGSCheckoutFormConfig

    /**
     * If true, checkout form will allow to make screenshots.
     */
    abstract val isScreenshotsAllowed: Boolean

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(id, environment.value, UUID.randomUUID().toString(), routeId)
    }
}