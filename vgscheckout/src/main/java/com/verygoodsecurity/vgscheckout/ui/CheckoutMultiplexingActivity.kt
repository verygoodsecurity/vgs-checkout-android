package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.requireExtra

internal class CheckoutMultiplexingActivity :
    BaseCheckoutActivity<VGSCheckoutMultiplexingConfiguration>() {

    override fun resolveConfig(key: String) =
        requireExtra<VGSCheckoutMultiplexingConfiguration>(key)

    override fun resolveCollect() = CollectProvider().get(this, config)

    override fun onPayClicked() {
        // TODO: Start multiplexing requests
        setResult(Activity.RESULT_OK, null)
        finish()
    }
}