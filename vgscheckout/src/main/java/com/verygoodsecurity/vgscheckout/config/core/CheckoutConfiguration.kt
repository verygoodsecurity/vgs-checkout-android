package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import java.util.*

internal const val DEFAULT_ENVIRONMENT = "sandbox"

abstract class CheckoutConfiguration internal constructor() : Parcelable {

    abstract val vaultID: String

    abstract val environment: String

    abstract val routeConfig: VGSCheckoutRouteConfiguration

    abstract val formConfig: VGSCheckoutFormConfiguration

    internal val analyticTracker: AnalyticTracker by lazy {
        DefaultAnalyticsTracker(vaultID, environment, UUID.randomUUID().toString())
    }
}