package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutExpirationDateOptions constructor(
    override val fieldName: String = "",
    val dateSeparateSerializer: VGSDateSeparateSerializer? = null,
    val inputFormatRegex: String = "MM/YY",
    val outputFormatRegex: String = "MM/YY"
) : ViewOptions()