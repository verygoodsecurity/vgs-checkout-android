package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

internal fun CheckoutFormConfig.isCardHolderVisible() =
    cardOptions.cardHolderOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE