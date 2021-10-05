package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import java.util.*

abstract class CheckoutConfig internal constructor() : Parcelable {

    abstract val vaultID: String

    abstract val environment: VGSCheckoutEnvironment

    abstract val routeConfig: CheckoutRouteConfig

    abstract val formConfig: CheckoutFormConfig

    abstract val isAnalyticsEnabled: Boolean

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(vaultID, environment.value, UUID.randomUUID().toString()).apply {
            isEnabled = isAnalyticsEnabled
        }
    }
}