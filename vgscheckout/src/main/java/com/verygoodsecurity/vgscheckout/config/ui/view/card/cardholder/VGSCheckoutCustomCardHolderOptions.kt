package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Card holder input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
class VGSCheckoutCustomCardHolderOptions constructor(
    override val fieldName: String = "",
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
) : CardHolderOptions()