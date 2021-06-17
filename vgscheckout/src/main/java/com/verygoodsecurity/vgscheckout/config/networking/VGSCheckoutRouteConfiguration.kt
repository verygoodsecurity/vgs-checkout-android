package com.verygoodsecurity.vgscheckout.config.networking

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutRouteConfiguration private constructor(
    val path: String,
    val hostnamePolicy: VGSCheckoutHostnamePolicy,
    val requestOptions: VGSCheckoutRequestOptions
) : Parcelable {

    class Builder {

        private var path: String = ""
        private var hostnamePolicy: VGSCheckoutHostnamePolicy = VGSCheckoutHostnamePolicy.Vault
        private var requestOptions = VGSCheckoutRequestOptions.Builder().build()

        fun setPath(path: String) = this.apply {
            this.path = path
        }

        fun setHostnamePolicy(policy: VGSCheckoutHostnamePolicy) = this.apply {
            this.hostnamePolicy = policy
        }

        fun setRequestOptions(config: VGSCheckoutRequestOptions) = this.apply {
            this.requestOptions = config
        }

        fun build() = VGSCheckoutRouteConfiguration(path, hostnamePolicy, requestOptions)
    }
}