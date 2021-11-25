package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

internal fun CheckoutFormConfig.isCardHolderHidden() =
    cardOptions.cardHolderOptions.visibility == VGSCheckoutFieldVisibility.HIDDEN

internal fun CheckoutFormConfig.isBillingAddressHidden() =
    addressOptions.visibility == VGSCheckoutBillingAddressVisibility.HIDDEN