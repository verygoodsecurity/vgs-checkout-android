package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val payButtonTitle: String?
) : CheckoutFormConfiguration() {

    class Builder {

        private var payButtonTitle: String? = null

        fun setPayButtonTitle(title: String) = this.apply {
            this.payButtonTitle = title
        }

        fun build() = VGSCheckoutMultiplexingFormConfiguration(payButtonTitle)
    }
}