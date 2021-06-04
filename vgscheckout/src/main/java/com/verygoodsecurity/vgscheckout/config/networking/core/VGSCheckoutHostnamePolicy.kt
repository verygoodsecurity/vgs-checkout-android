package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class VGSCheckoutHostnamePolicy : Parcelable {

    @Parcelize
    object Vault : VGSCheckoutHostnamePolicy()

    @Parcelize
    data class CustomHostname constructor(val hostname: String) : VGSCheckoutHostnamePolicy()

    @Parcelize
    data class Local constructor(val localhost: String, val port: Int) : VGSCheckoutHostnamePolicy()
}