package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import android.os.Bundle
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.InitEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.toCollectHTTPMethod
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy

internal class CheckoutActivity : BaseCheckoutActivity<VGSCheckoutConfiguration>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutConfiguration>(intent).config

    override fun resolveCollect() = CollectProvider().get(this, config)

    override fun handleSaveCard() {
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

    override fun hasCustomHeaders() = config.routeConfig.requestOptions.extraHeaders.isNotEmpty()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        config.analyticTracker.log(InitEvent(InitEvent.ConfigType.CUSTOM))
    }
}