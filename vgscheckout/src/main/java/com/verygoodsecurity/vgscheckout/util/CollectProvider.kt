package com.verygoodsecurity.vgscheckout.util

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.applyHostnamePolicy

/**
 * Helper class that responsible for providing VGSCollect instance with different setup, depending on RouteConfig.
 */
internal class CollectProvider {

    fun get(
        context: Context,
        config: CheckoutConfiguration
    ): VGSCollect {
        return VGSCollect.Builder(context, config.vaultID)
            .setEnvironment(config.environment.value)
            .setAnalyticTracker(config.analyticTracker)
            .applyHostnamePolicy(config.routeConfig.hostnamePolicy)
            .create()
    }
}