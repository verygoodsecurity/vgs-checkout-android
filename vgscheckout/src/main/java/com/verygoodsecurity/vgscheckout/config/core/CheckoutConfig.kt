package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.analytic.DefaultAnalyticsTracker
import com.verygoodsecurity.vgscheckout.collect.core.isSandbox
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedHostName
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedPort
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.*
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import java.util.*

/**
 * Base class of checkout config.
 *
 * @property id unique organization vault id.
 */
abstract class CheckoutConfig internal constructor(internal val id: String) : Parcelable {

    internal abstract val baseUrl: String

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
            val host = hostName.toHost().also {
                if (it != hostName) {
                    VGSCheckoutLogger.debug(message = "Hostname will be normalized to the $it")
                }
            }
            if (host.isValidIp()) {
                if (!host.isIpAllowed()) {
                    VGSCheckoutLogger.warn(message = "Current IP is not allowed, use localhost or private network IP")
                    return id.setupURL(environment)
                }
                if (!environment.isSandbox()) {
                    VGSCheckoutLogger.warn(message = ">Custom local IP and PORT can be used only in a sandbox environment.")
                    return id.setupURL(environment)
                }
                return host.setupLocalhostURL(port)
            } else {
                printPortDenied()
//fixme            cname = host
                return id.setupURL(environment)
            }
        } else {
            printPortDenied()
            return id.setupURL(environment)
        }
    }
}