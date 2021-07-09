package com.verygoodsecurity.vgscheckout.config.ui.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.postalcode.VGSCheckoutPostalCodeOptions

abstract class CheckoutFormConfiguration internal constructor() : Parcelable {

    abstract val cardNumberOptions: VGSCheckoutCardNumberOptions
    abstract val cardHolderOptions: VGSCheckoutCardHolderOptions
    abstract val cvcOptions: VGSCheckoutCVCOptions
    abstract val expirationDateOptions: VGSCheckoutExpirationDateOptions
    abstract val postalCodeOptions: VGSCheckoutPostalCodeOptions
    abstract val payButtonTitle: String?
}