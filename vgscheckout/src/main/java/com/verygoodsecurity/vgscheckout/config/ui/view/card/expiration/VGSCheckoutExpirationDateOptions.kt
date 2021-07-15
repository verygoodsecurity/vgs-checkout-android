package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutExpirationDateOptions private constructor(
    override val fieldName: String
) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun build() = VGSCheckoutExpirationDateOptions(fieldName)
    }
}