package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSVaultRequestConfig private constructor(
    val path: String,
    val extraData: Map<String, @RawValue Any?>,
    val mergePolicy: VGSCheckoutDataMergePolicy
) : RequestConfig() {

    class Builder {

        private var path = ""
        private var extraData = emptyMap<String, Any?>()
        private var mergePolicy = VGSCheckoutDataMergePolicy.FLAT

        fun setPath(path: String) = apply {
            this.path = path
        }

        fun setExtraData(data: Map<String, Any?>) = this.apply {
            this.extraData = data
        }

        fun setDataMergePolicy(policy: VGSCheckoutDataMergePolicy) = this.apply {
            this.mergePolicy = policy
        }

        fun build() = VGSVaultRequestConfig(path, extraData, mergePolicy)
    }
}