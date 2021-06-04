package com.verygoodsecurity.vgscheckout.config

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutVaultRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutVaultFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutVaultConfiguration private constructor(
    val routeConfig: VGSCheckoutVaultRouteConfiguration,
    val formConfig: VGSCheckoutVaultFormConfiguration,
) : Parcelable {

    class Builder {

        private var routeConfig = VGSCheckoutVaultRouteConfiguration.Builder().build()
        private var formConfig = VGSCheckoutVaultFormConfiguration.Builder().build()

        fun setRouteConfig(config: VGSCheckoutVaultRouteConfiguration) = this.also {
            this.routeConfig = config
        }

        fun setFormConfig(config: VGSCheckoutVaultFormConfiguration) = this.also {
            this.formConfig = config
        }

        fun build() = VGSCheckoutVaultConfiguration(routeConfig, formConfig)
    }
}