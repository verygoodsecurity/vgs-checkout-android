package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form card block UI options.
 *
 * @param cardNumberOptions holds card number input field UI options.
 * @param cardHolderOptions holds card holder name input field UI options.
 * @param cvcOptions holds card security code input field UI options.
 * @param expirationDateOptions holds expiration date input field UI options.
 */
@Parcelize
class VGSCheckoutCustomCardOptions @JvmOverloads constructor(
    override val cardNumberOptions: VGSCheckoutCardNumberOptions = VGSCheckoutCardNumberOptions(),
    override val cardHolderOptions: VGSCheckoutCardHolderOptions = VGSCheckoutCardHolderOptions(),
    override val cvcOptions: VGSCheckoutCVCOptions = VGSCheckoutCVCOptions(),
    override val expirationDateOptions: VGSCheckoutExpirationDateOptions = VGSCheckoutExpirationDateOptions()
) : CheckoutCardOptions()