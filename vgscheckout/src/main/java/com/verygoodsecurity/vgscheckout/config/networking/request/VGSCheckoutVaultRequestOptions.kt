package com.verygoodsecurity.vgscheckout.config.networking.request

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSCheckoutVaultRequestOptions private constructor(
    val httpMethod: VGSCheckoutHTTPMethod,
    val extraData: Map<String, @RawValue Any>,
    val mergePolicy: VGSCheckoutDataMergePolicy
) : Parcelable {

    class Builder {

        private var httpMethod = VGSCheckoutHTTPMethod.POST
        private var extraData = emptyMap<String, Any>()
        private var mergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON

        fun setHTTPMethod(method: VGSCheckoutHTTPMethod) = apply {
            this.httpMethod = method
        }

        fun setExtraData(data: Map<String, Any>) = this.apply {
            this.extraData = data
        }

        fun setDataMergePolicy(policy: VGSCheckoutDataMergePolicy) = this.apply {
            this.mergePolicy = policy
        }

        fun build() = VGSCheckoutVaultRequestOptions(httpMethod, extraData, mergePolicy)
    }
}