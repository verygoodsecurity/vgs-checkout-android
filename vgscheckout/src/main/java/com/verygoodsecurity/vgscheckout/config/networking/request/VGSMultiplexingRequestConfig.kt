package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Suppress("PARCELABLE_PRIMARY_CONSTRUCTOR_IS_EMPTY")
@Parcelize
class VGSMultiplexingRequestConfig private constructor(
    override val path: String = "",
    override val httpMethod: VGSCheckoutHTTPMethod = VGSCheckoutHTTPMethod.POST,
    override val extraData: Map<String, @RawValue Any?> = emptyMap(),
    override val mergePolicy: VGSCheckoutDataMergePolicy = VGSCheckoutDataMergePolicy.FLAT
) : RequestConfig() {


    class Builder {

        fun build() = VGSMultiplexingRequestConfig()
    }
}