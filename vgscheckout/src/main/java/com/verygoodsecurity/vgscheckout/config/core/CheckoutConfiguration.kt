package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import java.util.*

abstract class CheckoutConfiguration internal constructor() : Parcelable {

    abstract val vaultID: String

    abstract val environment: VGSCheckoutEnvironment

    abstract val routeConfig: VGSCheckoutRouteConfiguration

    abstract val formConfig: CheckoutFormConfiguration

    abstract val isAnalyticsEnabled: Boolean

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(vaultID, environment.value, UUID.randomUUID().toString()).apply {
            isEnabled = isAnalyticsEnabled
        }
    }
}