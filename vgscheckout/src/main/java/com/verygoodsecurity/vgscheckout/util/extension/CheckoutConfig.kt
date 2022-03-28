package com.verygoodsecurity.vgscheckout.util.extension

import android.content.Context
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.api.*
import com.verygoodsecurity.vgscheckout.collect.core.api.isURLValid
import com.verygoodsecurity.vgscheckout.collect.core.api.isValidPort
import com.verygoodsecurity.vgscheckout.collect.core.isSandbox
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedHostName
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedPort
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal fun CheckoutConfig.getBaseUrl(context: Context): String {
    val port = routeConfig.hostnamePolicy.getNormalizedPort()
    val hostName = routeConfig.hostnamePolicy.getNormalizedHostName()
    val environment = environment.value

    fun printPortDenied() {
        if (port.isValidPort()) {
            VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_port_is_not_allowed))
        }
    }

    if (!hostName.isNullOrBlank() && hostName.isURLValid()) {
        val host = hostName.toHost().also {
            if (it != hostName) {
                VGSCheckoutLogger.debug(message = "Hostname will be normalized to the $it")
            }
        }
        if (host.isValidIp()) {
            if (!host.isIpAllowed()) {
                VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_ip_is_not_allowed))
                return id.setupURL(environment)
            }
            if (!environment.isSandbox()) {
                VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_env_incorrect))
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
