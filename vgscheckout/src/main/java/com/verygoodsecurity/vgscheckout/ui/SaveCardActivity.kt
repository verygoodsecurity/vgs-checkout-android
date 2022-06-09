package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.InitEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment

internal class SaveCardActivity : BaseCheckoutActivity<VGSCheckoutAddCardConfig>() {

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
        val fragment = BaseFragment.create<SaveCardFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

    override fun navigateToPaymentMethods() {
        val fragment = BaseFragment.create<SelectPaymentMethodFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

}