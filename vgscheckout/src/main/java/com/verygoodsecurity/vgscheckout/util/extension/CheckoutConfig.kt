package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

internal fun CheckoutConfiguration.isCardHolderHidden() =
    this.formConfig.cardOptions.cardHolderOptions.visibility == VGSCheckoutFieldVisibility.HIDDEN