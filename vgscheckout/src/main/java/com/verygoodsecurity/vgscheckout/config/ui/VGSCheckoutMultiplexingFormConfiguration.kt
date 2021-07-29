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

    class Builder {

        private var cardOptions = VGSCheckoutCardOptions.Builder().build()
        private var addressOptions = VGSCheckoutBillingAddressOptions.Builder().build()
        private var payButtonTitle: String? = null

        fun setPayButtonTitle(title: String) = this.apply {
            this.payButtonTitle = title
        }

        fun build() = VGSCheckoutMultiplexingFormConfiguration(
            cardOptions,
            addressOptions,
            payButtonTitle
        )
    }
}