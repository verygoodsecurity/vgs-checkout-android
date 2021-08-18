package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.content.Intent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider

internal class CheckoutMultiplexingActivity :
    BaseCheckoutActivity<VGSCheckoutMultiplexingConfiguration>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutMultiplexingConfiguration>(intent).config

    override fun resolveCollect() = CollectProvider().get(this, config)

    override fun onPayClicked() {
        // TODO: Start multiplexing requests
        setResult(Activity.RESULT_OK, null)
        finish()
    }
}