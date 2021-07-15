package com.verygoodsecurity.vgscheckout.config.ui.view.card

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.postalcode.VGSCheckoutPostalCodeOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardOptions private constructor(
    val cardNumberOptions: VGSCheckoutCardNumberOptions,
    val cardHolderOptions: VGSCheckoutCardHolderOptions,
    val cvcOptions: VGSCheckoutCVCOptions,
    val expirationDateOptions: VGSCheckoutExpirationDateOptions,
    val postalCodeOptions: VGSCheckoutPostalCodeOptions
) : Parcelable {

    class Builder {

        private var cardNumberOptions = VGSCheckoutCardNumberOptions.Builder().build()
        private var cardHolderOptions = VGSCheckoutCardHolderOptions.Builder().build()
        private var cvcOptions = VGSCheckoutCVCOptions.Builder().build()
        private var expirationDateOptions = VGSCheckoutExpirationDateOptions.Builder().build()
        private var postalCodeOptions = VGSCheckoutPostalCodeOptions.Builder().build()

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

        fun build(): VGSCheckoutCardOptions = VGSCheckoutCardOptions(
            cardNumberOptions,
            cardHolderOptions,
            cvcOptions,
            expirationDateOptions,
            postalCodeOptions
        )
    }
}