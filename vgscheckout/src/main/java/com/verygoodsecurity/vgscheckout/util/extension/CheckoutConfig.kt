package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

internal fun CheckoutConfig.isCardHolderHidden() =
    this.formConfig.cardOptions.cardHolderOptions.visibility == VGSCheckoutFieldVisibility.HIDDEN