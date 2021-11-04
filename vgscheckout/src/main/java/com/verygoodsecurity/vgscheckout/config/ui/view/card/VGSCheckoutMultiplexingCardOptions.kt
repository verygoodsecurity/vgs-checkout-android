package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutMultiplexingCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutMultiplexingCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutMultiplexingCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutMultiplexingExpirationDateOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing flow checkout form card details section UI options.
 *
 * @param cardNumberOptions card number input field UI options.
 * @param cardHolderOptions card holder name input field UI options.
 * @param cvcOptions card security code input field UI options.
 * @param expirationDateOptions expiration date input field UI options.
 */
@Parcelize
class VGSCheckoutMultiplexingCardOptions private constructor(
    override val cardNumberOptions: VGSCheckoutMultiplexingCardNumberOptions,
    override val cardHolderOptions: VGSCheckoutMultiplexingCardHolderOptions,
    override val cvcOptions: VGSCheckoutMultiplexingCVCOptions,
    override val expirationDateOptions: VGSCheckoutMultiplexingExpirationDateOptions,
) : CheckoutCardOptions() {

    /**
     * Public constructor.
     *
     * Multiplexing flow has fixed requirements of payload etc. so this constructor does not allow to
     * override card details section UI options.
     */
    constructor() : this(
        VGSCheckoutMultiplexingCardNumberOptions(),
        VGSCheckoutMultiplexingCardHolderOptions(),
        VGSCheckoutMultiplexingCVCOptions(),
        VGSCheckoutMultiplexingExpirationDateOptions()
    )
}