package com.verygoodsecurity.vgscheckout.config.networking.request.core

import android.os.Parcelable

abstract class RequestConfig internal constructor() : Parcelable {

    abstract val path: String

    abstract val httpMethod: VGSCheckoutHTTPMethod

    abstract val extraData: Map<String, Any>

    abstract val mergePolicy: VGSCheckoutDataMergePolicy
}