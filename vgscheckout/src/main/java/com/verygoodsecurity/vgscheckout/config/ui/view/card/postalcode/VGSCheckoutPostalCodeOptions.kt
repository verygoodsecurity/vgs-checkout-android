package com.verygoodsecurity.vgscheckout.config.ui.view.card.postalcode

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutPostalCodeOptions private constructor(
    override val fieldName: String
) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun build() = VGSCheckoutPostalCodeOptions(fieldName)
    }
}