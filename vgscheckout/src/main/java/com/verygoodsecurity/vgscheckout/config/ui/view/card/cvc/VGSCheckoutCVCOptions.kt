package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Card security code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param isIconHidden defines if card brand icon should be hidden.
 */
@Parcelize
class VGSCheckoutCVCOptions constructor(
    override val fieldName: String = "",
    val isIconHidden: Boolean = false
) : ViewOptions()