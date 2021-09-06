package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutSecurityCodeOptions constructor(
    override val fieldName: String = "",
    val isIconHidden: Boolean = false
) : ViewOptions()