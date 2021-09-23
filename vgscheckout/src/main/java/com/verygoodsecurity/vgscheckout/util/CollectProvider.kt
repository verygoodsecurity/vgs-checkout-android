package com.verygoodsecurity.vgscheckout.util

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration

/**
 * Helper class that responsible for providing VGSCollect instance with different setup, depending on RouteConfig.
 */
internal class CollectProvider {

    // TODO: Generate different setups, depend on config(CNAME, Satellite etc.)
    fun get(
        context: Context,
        config: VGSCheckoutConfiguration
    ): VGSCollect {
        return VGSCollect.Builder(context, config.vaultID)
            .setEnvironment(config.environment)
            .setAnalyticTracker(config.analyticTracker)
            .create()
    }

    fun get(
        context: Context,
        config: VGSCheckoutMultiplexingConfiguration
    ): VGSCollect {
        return VGSCollect.Builder(context, config.vaultID)
            .setEnvironment(config.environment)
            .setAnalyticTracker(config.analyticTracker)
            .create()
    }
}