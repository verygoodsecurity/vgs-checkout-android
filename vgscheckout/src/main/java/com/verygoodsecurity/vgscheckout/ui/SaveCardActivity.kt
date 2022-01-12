package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import android.os.Bundle
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.InitEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

internal class SaveCardActivity :
    BaseCheckoutActivity<VGSCheckoutAddCardConfig>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutAddCardConfig>(intent).config

    override fun hasCustomHeaders() = false

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        config.analyticTracker.log(InitEvent(InitEvent.ConfigType.PAYOPT))
    }
}