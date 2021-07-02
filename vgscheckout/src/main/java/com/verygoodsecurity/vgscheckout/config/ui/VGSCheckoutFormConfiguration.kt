package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.postalcode.VGSCheckoutPostalCodeOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutFormConfiguration private constructor(
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

        fun setCardNumberOptions(options: VGSCheckoutCardNumberOptions) = this.apply {
            this.cardNumberOptions = options
        }

        fun setCardHolderOptions(options: VGSCheckoutCardHolderOptions) = this.apply {
            this.cardHolderOptions = options
        }

        fun setCVCOptions(options: VGSCheckoutCVCOptions) = this.apply {
            this.cvcOptions = options
        }

        fun setExpirationDateOptions(options: VGSCheckoutExpirationDateOptions) = this.apply {
            this.expirationDateOptions = options
        }

        fun setPostalCodeOptions(options: VGSCheckoutPostalCodeOptions) = this.apply {
            this.postalCodeOptions = options
        }

        fun setPayButtonTitle(title: String) = this.apply {
            this.payButtonTitle = title
        }

        fun build(): VGSCheckoutFormConfiguration = VGSCheckoutFormConfiguration(
            cardNumberOptions,
            cardHolderOptions,
            cvcOptions,
            expirationDateOptions,
            postalCodeOptions,
            payButtonTitle
        )
    }
}