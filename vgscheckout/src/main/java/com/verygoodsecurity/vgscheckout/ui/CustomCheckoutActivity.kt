package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import android.os.Bundle
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.InitEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

internal class CustomCheckoutActivity : BaseCheckoutActivity<VGSCheckoutCustomConfig>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutCustomConfig>(intent).config

    override fun hasCustomHeaders() = config.routeConfig.requestOptions.extraHeaders.isNotEmpty()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        config.analyticTracker.log(InitEvent(InitEvent.ConfigType.CUSTOM))
    }
}