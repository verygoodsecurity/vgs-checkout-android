package com.verygoodsecurity.vgscheckout.networking

import androidx.core.util.PatternsCompat
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

private const val HTTP_SCHEME = "http://"
private const val HTTPS_SCHEME = "https://"

/** @suppress */
internal fun String.setupLocalhostURL(port: Int?): String {
    val divider = ":"

    val prt = if (!port.isValidPort()) {
        VGSCheckoutLogger.warn(message = "Port is not valid")
        ""
    } else {
        divider + port
    }
    return StringBuilder(HTTP_SCHEME)
        .append(this)
        .append(prt)
        .toString()
}

/** @suppress */
internal fun String.setupURL(rawValue: String, isPaymentUrl: Boolean): String {
    return when {
        this.isEmpty() || !isTenantIdValid() -> {
            VGSCheckoutLogger.warn(message = "Vault ID is not valid")
            return ""
        }
        rawValue.isEmpty() || !rawValue.isEnvironmentValid() -> {
            VGSCheckoutLogger.warn(message = "Environment is not valid")
            return ""
        }
        else -> this.buildURL(rawValue, isPaymentUrl)
    }
}

private val PAYMENT_URL_ROUTE_ID = "4880868f-d88b-4333-ab70-d9deecdbffc4"

private fun String.buildURL(env: String, isPaymentUrl: Boolean): String {
    val DOMEN = "verygoodproxy.com"
    val DIVIDER = "."
    val SCHEME = "https://"

    val builder = StringBuilder(SCHEME)
    builder.append(this)
    if (isPaymentUrl) builder.append("-$PAYMENT_URL_ROUTE_ID")
    builder.append(DIVIDER)
    builder.append(env)
    builder.append(DIVIDER)
    builder.append(DOMEN)

    return builder.toString()
}

internal fun String.isTenantIdValid(): Boolean =
    Pattern.compile("^[a-zA-Z0-9]*\$").matcher(this).matches()

internal fun String.isEnvironmentValid(): Boolean =
    Pattern.compile("^(live|sandbox|LIVE|SANDBOX)+((-)+([a-zA-Z0-9]+)|)+\$").matcher(this).matches()

internal fun String?.isUrlValid(): Boolean {
    return when {
        isNullOrBlank() -> false
        else -> PatternsCompat.WEB_URL.matcher(this).matches()
    }
}

internal fun String.isValidIp(): Boolean {
    return when {
        isNullOrEmpty() -> false
        else -> PatternsCompat.IP_ADDRESS.matcher(this).matches()
    }
}

private const val AVD_LOCALHOST_ALIAS = "10.0.2.2"
private const val GENYMOTION_LOCALHOST_ALIAS = "10.0.3.2"
private const val PRIVATE_NETWORK_IP_PREFIX = "192.168."

internal fun String.isIpAllowed() = this == AVD_LOCALHOST_ALIAS ||
        this == GENYMOTION_LOCALHOST_ALIAS ||
        this.startsWith(PRIVATE_NETWORK_IP_PREFIX)

internal const val PORT_MIN_VALUE = 1L
internal const val PORT_MAX_VALUE = 65353L

internal fun Int?.isValidPort(): Boolean = this != null && this in PORT_MIN_VALUE..PORT_MAX_VALUE

internal fun String.toHostnameValidationUrl(tnt: String): String {
    return String.format(
        "https://js.verygoodvault.com/collect-configs/%s__%s.txt",
        this.toHost(),
        tnt
    )
}

internal infix fun String.equalsUrl(name: String?): Boolean {
    return toHost() == name?.toHost()
}

internal fun String.toHttps(): String {
    return when {
        startsWith(HTTP_SCHEME) -> this
        startsWith(HTTPS_SCHEME) -> this
        else -> "$HTTPS_SCHEME$this"
    }
}

internal fun String.toHost(): String {
    return try {
        URL(this.toHttps()).host
    } catch (e: MalformedURLException) {
        ""
    }
}
