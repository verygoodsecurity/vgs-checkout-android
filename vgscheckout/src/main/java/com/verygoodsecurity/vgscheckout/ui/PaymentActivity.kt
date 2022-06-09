package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.InitEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveAndPayFragment

internal class PaymentActivity : BaseCheckoutActivity<VGSCheckoutPaymentConfig>() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        config.analyticTracker.log(InitEvent(InitEvent.ConfigType.PAYOPT, config))
    }

    override fun initFragment() {
        if (config.savedCards.isEmpty()) {
            super.initFragment()
            return
        }
        navigateToPaymentMethods()
    }

    override fun navigateToSaveCard() {
        val fragment = BaseFragment.create<SaveAndPayFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }
}