package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCustomCardOptions @JvmOverloads constructor(
    override val cardNumberOptions: VGSCheckoutCardNumberOptions = VGSCheckoutCardNumberOptions(),
    override val cardHolderOptions: VGSCheckoutCardHolderOptions = VGSCheckoutCardHolderOptions(),
    override val cvcOptions: VGSCheckoutCVCOptions = VGSCheckoutCVCOptions(),
    override val expirationDateOptions: VGSCheckoutExpirationDateOptions = VGSCheckoutExpirationDateOptions()
) : CheckoutCardOptions()