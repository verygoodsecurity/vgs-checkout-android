package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutPaymentCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutPaymentCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutPaymentCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutPaymentExpirationDateOptions
import kotlinx.parcelize.Parcelize

/**
 * Payment flow checkout form card details section UI options.
 *
 * @param cardNumberOptions card number input field UI options.
 * @param cardHolderOptions card holder name input field UI options.
 * @param cvcOptions card security code input field UI options.
 * @param expirationDateOptions expiration date input field UI options.
 */
@Parcelize
class VGSCheckoutPaymentCardOptions private constructor(
    override val cardNumberOptions: VGSCheckoutPaymentCardNumberOptions,
    override val cardHolderOptions: VGSCheckoutPaymentCardHolderOptions,
    override val cvcOptions: VGSCheckoutPaymentCVCOptions,
    override val expirationDateOptions: VGSCheckoutPaymentExpirationDateOptions,
) : CheckoutCardOptions() {

    /**
     * Public constructor.
     *
     * Payment orchestration flow has fixed requirements of payload etc. so this constructor does not allow to
     * override card details section UI options.
     */
    constructor() : this(
        VGSCheckoutPaymentCardNumberOptions(),
        VGSCheckoutPaymentCardHolderOptions(),
        VGSCheckoutPaymentCVCOptions(),
        VGSCheckoutPaymentExpirationDateOptions()
    )
}