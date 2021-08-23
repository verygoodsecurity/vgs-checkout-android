package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutFormConfiguration @JvmOverloads constructor(
    override val cardOptions: VGSCheckoutCardOptions = VGSCheckoutCardOptions(),
    override val addressOptions: VGSCheckoutBillingAddressOptions = VGSCheckoutBillingAddressOptions(),
    override val payButtonTitle: String? = null
) : CheckoutFormConfiguration()