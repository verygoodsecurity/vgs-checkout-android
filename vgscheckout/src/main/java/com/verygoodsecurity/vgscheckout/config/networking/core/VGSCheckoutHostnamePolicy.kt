package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Define base url to send data.
 */
sealed class VGSCheckoutHostnamePolicy : Parcelable {

    /**
     * Default vault url.
     */
    @Parcelize
    object Vault : VGSCheckoutHostnamePolicy()

    /**
     * Custom url hostname. Should be configured on the dashboard.
     *
     * @param hostname custom hostname.
     */
    @Parcelize
    data class CustomHostname constructor(val hostname: String) : VGSCheckoutHostnamePolicy()

    /**
     *  Locally configured [VGS-Satellite](https://www.verygoodsecurity.com/docs/vgs-satellite) url.
     *
     *  IMPORTANT: VGS-Satellite uses http protocol, starting from android 8.1(27) communication using
     *  http protocol is disabled by default, developer must add network security config to allow it
     *  or allow clear text traffic in manifest.
     *  Read more https://developer.android.com/training/articles/security-config#CleartextTrafficPermitted.
     *
     *  @param localhost ip.
     *  @param port integer value from 1 to 65353.
     */
    @Parcelize
    data class Local constructor(val localhost: String, val port: Int) : VGSCheckoutHostnamePolicy()
}

//fixme remove collect
internal fun VGSCheckoutHostnamePolicy.getNormalizedHostName(): String? {
    return when (this) {
        is VGSCheckoutHostnamePolicy.Vault -> null
        is VGSCheckoutHostnamePolicy.CustomHostname -> this.hostname
        is VGSCheckoutHostnamePolicy.Local -> this.localhost
    }
}

internal fun VGSCheckoutHostnamePolicy.getNormalizedPort(): Int? {
    return when (this) {
        is VGSCheckoutHostnamePolicy.Vault -> null
        is VGSCheckoutHostnamePolicy.CustomHostname -> null
        is VGSCheckoutHostnamePolicy.Local -> this.port
    }
}