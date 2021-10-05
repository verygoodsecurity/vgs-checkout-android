package com.verygoodsecurity.vgscheckout.config.networking.request.core

import android.os.Parcelable

abstract class CheckoutRequestOptions : Parcelable {

    abstract val httpMethod: VGSCheckoutHTTPMethod

    abstract val extraHeaders: Map<String, String>

    abstract val extraData: Map<String, Any>

    abstract val mergePolicy: VGSCheckoutDataMergePolicy
}