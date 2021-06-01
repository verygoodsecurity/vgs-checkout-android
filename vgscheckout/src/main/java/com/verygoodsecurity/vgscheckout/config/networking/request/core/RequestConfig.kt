package com.verygoodsecurity.vgscheckout.config.networking.request.core

import android.os.Parcelable
import kotlinx.parcelize.RawValue

abstract class RequestConfig internal constructor() : Parcelable {

    abstract val path: String
    abstract val httpMethod: VGSCheckoutHTTPMethod
    abstract val extraData: Map<String, @RawValue Any?>
    abstract val mergePolicy: VGSCheckoutDataMergePolicy
}