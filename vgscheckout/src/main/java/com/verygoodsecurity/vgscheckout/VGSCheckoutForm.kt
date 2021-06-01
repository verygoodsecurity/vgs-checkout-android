package com.verygoodsecurity.vgscheckout

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSVaultRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.RouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSUserInterfaceConfig
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutForm private constructor(
    val tenantID: String,
    val routeConfig: RouteConfig<*>,
    val uiConfig: VGSUserInterfaceConfig
) : Parcelable {

    fun start(context: Context) {
        CheckoutActivity.start(context, this)
    }

    fun startForResult(activity: Activity, requestCode: Int) {
        CheckoutActivity.startForResult(activity, requestCode, this)
    }

    class Builder constructor(private val tenantID: String) {

        private var routeConfig: RouteConfig<*> = VGSVaultRouteConfig.Builder().build()
        private var uiConfig = VGSUserInterfaceConfig.Builder().build()

        fun setRouteConfig(config: RouteConfig<*>) = this.apply {
            this.routeConfig = config
        }

        fun setUserInterfaceConfig(config: VGSUserInterfaceConfig) = this.apply {
            this.uiConfig = config
        }

        fun build(): VGSCheckoutForm = VGSCheckoutForm(tenantID, routeConfig, uiConfig)
    }
}