package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

internal class PaymentActivity : BaseCheckoutActivity<VGSCheckoutPaymentConfig>() {

    @Suppress("RedundantOverride")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        // TODO: Send analytics
    }

    override fun initFragment() {
        if (config.savedCards.isEmpty()) {
            super.initFragment()
            return
        }
        navigateToPaymentMethods()
    }
}