package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.analytic.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.collect.core.isSandbox
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedHostName
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedPort
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.*
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
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
        DefaultAnalyticsTracker(id, environment.value, UUID.randomUUID().toString())
    }

    protected fun generateBaseUrl(): String {
        val port = routeConfig.hostnamePolicy.getNormalizedPort()
        val hostName = routeConfig.hostnamePolicy.getNormalizedHostName()
        val environment = environment.value

        fun printPortDenied() {
            if (port.isValidPort()) {
                VGSCheckoutLogger.warn(message = "To protect your device we allow to use PORT only on localhost. PORT will be ignored.")
            }
        }

        if (!hostName.isNullOrBlank() && hostName.isUrlValid()) {
            val host = hostName.toHost()
            if (host != hostName) {
                VGSCheckoutLogger.debug(message = "Hostname will be normalized to the $host")
            }
            if (host.isValidIp()) {
                if (!host.isIpAllowed()) {
                    VGSCheckoutLogger.warn(message = "Current IP is not allowed, use localhost or private network IP")
                    return id.setupURL(environment, routeId)
                }
                if (!environment.isSandbox()) {
                    VGSCheckoutLogger.warn(message = ">Custom local IP and PORT can be used only in a sandbox environment.")
                    return id.setupURL(environment, routeId)
                }
                return host.setupLocalhostURL(port)
            } else {
                printPortDenied()
//fixme            cname = host
                return id.setupURL(environment, routeId)
            }
        } else {
            printPortDenied()
            return id.setupURL(environment, routeId)
        }
    }
}