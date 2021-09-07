package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.DateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutExpirationDateOptions constructor(
    override val fieldName: String = "",
    val dateSeparateSerializer: DateSeparateSerializer? = null,
    val inputFormatRegex: String? = null,
    val outputFormatRegex: String? = null
) : ViewOptions()