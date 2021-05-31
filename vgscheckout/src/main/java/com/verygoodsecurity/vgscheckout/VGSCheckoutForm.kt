package com.verygoodsecurity.vgscheckout

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutForm private constructor(val tenantID: String) : Parcelable {

    fun start(context: Context) {
        CheckoutActivity.start(context, this)
    }

    fun startForResult(activity: Activity, requestCode: Int) {
        CheckoutActivity.startForResult(activity, requestCode, this)
    }

    class Builder constructor(private val tenantID: String) {

        fun build(): VGSCheckoutForm = VGSCheckoutForm(tenantID)
    }
}