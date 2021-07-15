package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCVCOptions private constructor(override val fieldName: String) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun build() = VGSCheckoutCVCOptions(fieldName)
    }
}