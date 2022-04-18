package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.collect.core.analytic.event.InitEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

internal class SaveCardActivity : BaseCheckoutActivity<VGSCheckoutAddCardConfig>() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        config.analyticTracker.log(InitEvent(InitEvent.ConfigType.PAYOPT))
    }

    override fun initFragment() {
        if (config.savedCards.isEmpty()) {
            super.initFragment()
            return
        }
        navigateToPaymentMethods()
    }
}