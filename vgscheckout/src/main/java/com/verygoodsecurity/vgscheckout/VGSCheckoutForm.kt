package com.verygoodsecurity.vgscheckout

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.VGSUserInterfaceConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutForm private constructor(
    val tenantID: String,
    val uiConfig: VGSUserInterfaceConfig
) : Parcelable {

    fun start(context: Context) {
        CheckoutActivity.start(context, this)
    }

    fun startForResult(activity: Activity, requestCode: Int) {
        CheckoutActivity.startForResult(activity, requestCode, this)
    }

    class Builder constructor(private val tenantID: String) {

        private var uiConfig = VGSUserInterfaceConfig.Builder().build()

        fun setUserInterfaceConfig(config: VGSUserInterfaceConfig) = this.apply {
            this.uiConfig = config
        }

        fun build(): VGSCheckoutForm = VGSCheckoutForm(tenantID, uiConfig)
    }
}