package com.verygoodsecurity.vgscheckout.config.networking.request

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSCheckoutRequestOptions @JvmOverloads constructor(
    val httpMethod: VGSCheckoutHTTPMethod = VGSCheckoutHTTPMethod.POST,
    val extraData: Map<String, @RawValue Any> = emptyMap(),
    val mergePolicy: VGSCheckoutDataMergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON
) : Parcelable