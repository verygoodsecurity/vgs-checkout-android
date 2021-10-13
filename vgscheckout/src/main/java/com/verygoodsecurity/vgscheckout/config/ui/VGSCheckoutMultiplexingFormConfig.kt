package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutMultiplexingBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutMultiplexingCardOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing flow checkout form configuration.
 *
 * @param cardOptions credit card details block UI options.
 * @param addressOptions address details block UI options.
 */
@Parcelize
class VGSCheckoutMultiplexingFormConfig private constructor(
    override val cardOptions: VGSCheckoutMultiplexingCardOptions,
    override val addressOptions: VGSCheckoutMultiplexingBillingAddressOptions
) : CheckoutFormConfig() {

    /**
     * Public constructor.
     *
     * @param addressOptions address details block UI options.
     */
    @JvmOverloads
    constructor(
        addressOptions: VGSCheckoutMultiplexingBillingAddressOptions = VGSCheckoutMultiplexingBillingAddressOptions()
    ) : this(VGSCheckoutMultiplexingCardOptions(), addressOptions)
}