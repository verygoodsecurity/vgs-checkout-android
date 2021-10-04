package com.verygoodsecurity.vgscheckout.config.ui.view.card.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions

abstract class CheckoutCardOptions : Parcelable {

    abstract val cardNumberOptions: VGSCheckoutCardNumberOptions

    abstract val cardHolderOptions: VGSCheckoutCardHolderOptions

    abstract val cvcOptions: VGSCheckoutCVCOptions

    abstract val expirationDateOptions: VGSCheckoutExpirationDateOptions
}