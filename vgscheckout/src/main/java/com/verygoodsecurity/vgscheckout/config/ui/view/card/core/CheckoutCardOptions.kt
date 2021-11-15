package com.verygoodsecurity.vgscheckout.config.ui.view.card.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.CardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.CardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.CVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.ExpirationDateOptions

/**
 * Base class of card details section UI options.
 */
abstract class CheckoutCardOptions : Parcelable {

    /**
     * Holds card number input field UI options.
     */
    abstract val cardNumberOptions: CardNumberOptions

    /**
     * Holds card holder name input field UI options.
     */
    abstract val cardHolderOptions: CardHolderOptions

    /**
     * Holds card security code input field UI options.
     */
    abstract val cvcOptions: CVCOptions

    /**
     * Holds expiration date input field UI options.
     */
    abstract val expirationDateOptions: ExpirationDateOptions
}