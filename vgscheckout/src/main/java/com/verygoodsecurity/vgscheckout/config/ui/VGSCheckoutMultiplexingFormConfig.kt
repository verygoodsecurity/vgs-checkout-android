package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutMultiplexingBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutMultiplexingCardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfig private constructor(
    override val cardOptions: VGSCheckoutMultiplexingCardOptions,
    override val addressOptions: VGSCheckoutMultiplexingBillingAddressOptions
) : CheckoutFormConfig() {

    @JvmOverloads
    constructor(
        addressOptions: VGSCheckoutMultiplexingBillingAddressOptions = VGSCheckoutMultiplexingBillingAddressOptions()
    ) : this(VGSCheckoutMultiplexingCardOptions(), addressOptions)
}