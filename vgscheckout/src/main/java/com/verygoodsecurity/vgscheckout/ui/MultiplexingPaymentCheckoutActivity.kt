package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingPaymentConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

internal class MultiplexingPaymentCheckoutActivity :
    BaseCheckoutActivity<VGSCheckoutMultiplexingPaymentConfig>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutMultiplexingPaymentConfig>(intent).config

    override fun hasCustomHeaders() = false
}