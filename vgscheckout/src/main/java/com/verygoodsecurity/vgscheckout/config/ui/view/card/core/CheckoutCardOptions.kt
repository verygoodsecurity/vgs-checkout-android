package com.verygoodsecurity.vgscheckout.config.ui.view.card.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions

/**
 * Base class of card block UI options.
 */
abstract class CheckoutCardOptions : Parcelable {

    /**
     * Holds card number input field UI options.
     */
    abstract val cardNumberOptions: VGSCheckoutCardNumberOptions

    /**
     * Holds card holder name input field UI options.
     */
    abstract val cardHolderOptions: VGSCheckoutCardHolderOptions

    /**
     * Holds card security code input field UI options.
     */
    abstract val cvcOptions: VGSCheckoutCVCOptions

    /**
     * Holds expiration date input field UI options.
     */
    abstract val expirationDateOptions: VGSCheckoutExpirationDateOptions
}