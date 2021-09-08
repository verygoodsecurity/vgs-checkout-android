package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCVCOptions constructor(
    override val fieldName: String = "",
    val isIconHidden: Boolean = false
) : ViewOptions()