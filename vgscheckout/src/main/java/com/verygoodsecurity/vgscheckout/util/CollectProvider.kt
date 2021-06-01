package com.verygoodsecurity.vgscheckout.util

import android.content.Context
import com.verygoodsecurity.vgscheckout.VGSCheckoutForm
import com.verygoodsecurity.vgscollect.core.VGSCollect

/**
 * Helper class that responsible for providing VGSCollect instance with different setup, depending on RouteConfig.
 */
internal class CollectProvider {

    fun get(context: Context, config: VGSCheckoutForm): VGSCollect {
        return VGSCollect.Builder(context, config.tenantID)
            .setEnvironment(config.routeConfig.environment)
            .create()
    }
}