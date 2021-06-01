package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSVaultRequestConfig private constructor(
    override val path: String,
    override val httpMethod: VGSCheckoutHTTPMethod,
    override val extraData: Map<String, @RawValue Any>,
    override val mergePolicy: VGSCheckoutDataMergePolicy
) : RequestConfig() {

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

        fun build() = VGSVaultRequestConfig(path, httpMethod, extraData, mergePolicy)
    }
}