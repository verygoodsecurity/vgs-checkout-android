package com.verygoodsecurity.vgscheckout.config.networking.request

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSCheckoutVaultRequestOptions private constructor(
    val path: String,
    val httpMethod: VGSCheckoutHTTPMethod,
    val extraData: Map<String, @RawValue Any>,
    val mergePolicy: VGSCheckoutDataMergePolicy
) : Parcelable {

    class Builder {

        private var path = ""
        private var httpMethod = VGSCheckoutHTTPMethod.POST
        private var extraData = emptyMap<String, Any>()
        private var mergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON

        fun setPath(path: String) = apply {
            this.path = path
        }

        fun setHTTPMethod(method: VGSCheckoutHTTPMethod) = apply {
            this.httpMethod = method
        }

        fun setExtraData(data: Map<String, Any>) = this.apply {
            this.extraData = data
        }

        fun setDataMergePolicy(policy: VGSCheckoutDataMergePolicy) = this.apply {
            this.mergePolicy = policy
        }

        fun build() = VGSCheckoutVaultRequestOptions(path, httpMethod, extraData, mergePolicy)
    }
}