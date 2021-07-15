package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutFormConfiguration private constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutAddressOptions,
    override val payButtonTitle: String?
) : CheckoutFormConfiguration() {

    class Builder {

        private var cardOptions = VGSCheckoutCardOptions.Builder().build()
        private var addressOptions = VGSCheckoutAddressOptions.Builder().build()
        private var payButtonTitle: String? = null

        fun setCardOptions(options: VGSCheckoutCardOptions) = this.apply {
            this.cardOptions = options
        }

        fun setAddressOptions(options: VGSCheckoutAddressOptions) = this.apply {
            this.addressOptions = options
        }

        fun setPayButtonTitle(title: String) = this.apply {
            this.payButtonTitle = title
        }

        fun build(): VGSCheckoutFormConfiguration = VGSCheckoutFormConfiguration(
            cardOptions,
            addressOptions,
            payButtonTitle
        )
    }
}