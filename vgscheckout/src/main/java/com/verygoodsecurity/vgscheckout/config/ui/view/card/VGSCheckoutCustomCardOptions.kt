package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCustomCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCustomCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCustomCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutCustomExpirationDateOptions
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
class VGSCheckoutCustomCardOptions @JvmOverloads constructor(
    override val cardNumberOptions: VGSCheckoutCustomCardNumberOptions = VGSCheckoutCustomCardNumberOptions(),
    override val cardHolderOptions: VGSCheckoutCustomCardHolderOptions = VGSCheckoutCustomCardHolderOptions(),
    override val cvcOptions: VGSCheckoutCustomCVCOptions = VGSCheckoutCustomCVCOptions(),
    override val expirationDateOptions: VGSCheckoutCustomExpirationDateOptions = VGSCheckoutCustomExpirationDateOptions()
) : CheckoutCardOptions()