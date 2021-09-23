package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutExpirationDateOptions constructor(
    override val fieldName: String = "",
    val dateSeparateSerializer: VGSDateSeparateSerializer? = null,
    val inputFormatRegex: String = DATE_FORMAT,
    val outputFormatRegex: String = DATE_FORMAT
) : ViewOptions() {

    companion object {

        private const val DATE_FORMAT = "MM/yy"
    }
}