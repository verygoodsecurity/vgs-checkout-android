package com.verygoodsecurity.vgscheckout.ui

import android.os.Bundle
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.core.requireExtra

class CheckoutMultiplexingActivity : BaseCheckoutActivity<VGSCheckoutMultiplexingConfiguration>() {

    override fun getConfig(key: String): VGSCheckoutMultiplexingConfiguration = requireExtra(key)

    override fun initView(savedInstanceState: Bundle?) {}

    override fun onPayClicked() {
        // TODO: Implement
    }
}