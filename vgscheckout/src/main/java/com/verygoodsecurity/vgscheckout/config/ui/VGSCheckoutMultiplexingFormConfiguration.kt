package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.postalcode.VGSCheckoutPostalCodeOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val cardNumberOptions: VGSCheckoutCardNumberOptions,
    override val cardHolderOptions: VGSCheckoutCardHolderOptions,
    override val cvcOptions: VGSCheckoutCVCOptions,
    override val expirationDateOptions: VGSCheckoutExpirationDateOptions,
    override val postalCodeOptions: VGSCheckoutPostalCodeOptions,
    override val payButtonTitle: String?
) : CheckoutFormConfiguration() {

    class Builder {

        private var cardNumberOptions = VGSCheckoutCardNumberOptions.Builder().build()
        private var cardHolderOptions = VGSCheckoutCardHolderOptions.Builder().build()
        private var cvcOptions = VGSCheckoutCVCOptions.Builder().build()
        private var expirationDateOptions = VGSCheckoutExpirationDateOptions.Builder().build()
        private var postalCodeOptions = VGSCheckoutPostalCodeOptions.Builder().build()
        private var payButtonTitle: String? = null

        fun setPayButtonTitle(title: String) = this.apply {
            this.payButtonTitle = title
        }

        fun build() = VGSCheckoutMultiplexingFormConfiguration(
            cardNumberOptions,
            cardHolderOptions,
            cvcOptions,
            expirationDateOptions,
            postalCodeOptions,
            payButtonTitle
        )
    }
}