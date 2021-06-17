package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.requireExtra
import com.verygoodsecurity.vgscheckout.util.extension.toCollectHTTPMethod
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.view.CheckoutView
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest

internal class CheckoutActivity : BaseCheckoutActivity<VGSCheckoutConfiguration>() {

    override fun resolveConfig(key: String): VGSCheckoutConfiguration = requireExtra(key)

    override fun resolveCollect() = CollectProvider().get(this, config)

    override fun initView(savedInstanceState: Bundle?) {
        findViewById<CheckoutView>(R.id.cvForm)?.let {
            it.applyConfig(config.formConfig)
            it.bindViews(collect)
            it.onPayListener = this
        }
    }

    override fun onPayClicked() {
        asyncSubmit()
    }

    private fun asyncSubmit() {
        with(config.routeConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath(path)
                    .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                    .setCustomData(requestOptions.extraData)
                    .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                    .build()
            )
        }
    }
}