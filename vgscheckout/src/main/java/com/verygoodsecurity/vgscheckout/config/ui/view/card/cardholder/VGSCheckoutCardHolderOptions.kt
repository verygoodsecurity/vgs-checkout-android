package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Card holder input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if card holder name input field should be visible to user.
 */
@Parcelize
class VGSCheckoutCardHolderOptions constructor(
    override val fieldName: String = "",
    val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
) : ViewOptions()