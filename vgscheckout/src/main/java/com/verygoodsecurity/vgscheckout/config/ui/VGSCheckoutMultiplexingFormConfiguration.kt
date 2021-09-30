package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutMultiplexingBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutMultiplexingCardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val cardOptions: VGSCheckoutMultiplexingCardOptions = VGSCheckoutMultiplexingCardOptions(),
    override val addressOptions: VGSCheckoutMultiplexingBillingAddressOptions = VGSCheckoutMultiplexingBillingAddressOptions()
) : CheckoutFormConfiguration() {

    @JvmOverloads
    constructor(
        addressOptions: VGSCheckoutMultiplexingBillingAddressOptions = VGSCheckoutMultiplexingBillingAddressOptions()
    ) : this(VGSCheckoutMultiplexingCardOptions(), addressOptions)
}