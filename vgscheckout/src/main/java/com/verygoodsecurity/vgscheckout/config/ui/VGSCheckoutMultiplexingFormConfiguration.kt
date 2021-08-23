package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutBillingAddressOptions,
    override val payButtonTitle: String?
) : CheckoutFormConfiguration() {

    @JvmOverloads
    constructor(payButtonTitle: String? = null) : this(
        VGSCheckoutCardOptions(),
        VGSCheckoutBillingAddressOptions(),
        payButtonTitle,
    )
}