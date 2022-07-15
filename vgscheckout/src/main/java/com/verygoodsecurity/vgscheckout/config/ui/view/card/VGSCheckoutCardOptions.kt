package com.verygoodsecurity.vgscheckout.config.ui.view.card

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form card details section UI options.
 *
 * @param cardNumberOptions card number input field UI options.
 * @param cardHolderOptions card holder name input field UI options.
 * @param cvcOptions card security code input field UI options.
 * @param expirationDateOptions expiration date input field UI options.
 */
@Parcelize
class VGSCheckoutCardOptions constructor(
    val cardNumberOptions: VGSCheckoutCardNumberOptions,
    val cardHolderOptions: VGSCheckoutCardHolderOptions,
    val cvcOptions: VGSCheckoutCVCOptions,
    val expirationDateOptions: VGSCheckoutExpirationDateOptions
) : Parcelable