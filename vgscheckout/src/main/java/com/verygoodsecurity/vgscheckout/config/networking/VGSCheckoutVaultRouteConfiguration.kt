package com.verygoodsecurity.vgscheckout.config.networking

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutVaultRequestOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutVaultRouteConfiguration private constructor(
    val path: String,
    val hostnamePolicy: VGSCheckoutHostnamePolicy,
    val requestOptions: VGSCheckoutVaultRequestOptions
) : Parcelable {

    class Builder {

        private var path: String = ""
        private var hostnamePolicy: VGSCheckoutHostnamePolicy = VGSCheckoutHostnamePolicy.Vault
        private var requestOptions = VGSCheckoutVaultRequestOptions.Builder().build()

        fun setPath(path: String) = this.apply {
            this.path = path
        }

        fun setHostnamePolicy(policy: VGSCheckoutHostnamePolicy) = this.apply {
            this.hostnamePolicy = policy
        }

        fun setRequestOptions(config: VGSCheckoutVaultRequestOptions) = this.apply {
            this.requestOptions = config
        }

        fun build() = VGSCheckoutVaultRouteConfiguration(path, hostnamePolicy, requestOptions)
    }
}