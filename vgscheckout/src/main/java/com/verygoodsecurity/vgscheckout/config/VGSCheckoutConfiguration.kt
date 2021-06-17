package com.verygoodsecurity.vgscheckout.config

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutConfiguration private constructor(
    val routeConfig: VGSCheckoutRouteConfiguration,
    val formConfig: VGSCheckoutFormConfiguration,
) : Parcelable {

    class Builder {

        private var routeConfig = VGSCheckoutRouteConfiguration.Builder().build()
        private var formConfig = VGSCheckoutFormConfiguration.Builder().build()

        fun setRouteConfig(config: VGSCheckoutRouteConfiguration) = this.also {
            this.routeConfig = config
        }

        fun setFormConfig(config: VGSCheckoutFormConfiguration) = this.also {
            this.formConfig = config
        }

        fun build() = VGSCheckoutConfiguration(routeConfig, formConfig)
    }
}