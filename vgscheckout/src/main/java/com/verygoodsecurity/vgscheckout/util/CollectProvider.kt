package com.verygoodsecurity.vgscheckout.util

import android.content.Context
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscollect.core.VGSCollect

/**
 * Helper class that responsible for providing VGSCollect instance with different setup, depending on RouteConfig.
 */
internal class CollectProvider {

    fun get(
        context: Context,
        vaultID: String,
        environment: String,
        config: CheckoutConfiguration
    ): VGSCollect {
        return VGSCollect.Builder(context, vaultID)
            .setEnvironment(environment)
            .create()
    }
}