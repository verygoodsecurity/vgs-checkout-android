package com.verygoodsecurity.vgscheckout.config

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutVaultRouteConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutVaultConfiguration private constructor(
    val routeConfig: VGSCheckoutVaultRouteConfiguration
) : Parcelable {

    class Builder {

        private var routeConfig = VGSCheckoutVaultRouteConfiguration.Builder().build()

        fun setRouteConfig(config: VGSCheckoutVaultRouteConfiguration) = this.also {
            this.routeConfig = config
        }

        fun build() = VGSCheckoutVaultConfiguration(routeConfig)
    }
}