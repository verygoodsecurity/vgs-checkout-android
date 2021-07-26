package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCVCOptions private constructor(
    override val fieldName: String,
    val isIconHidden: Boolean
) : ViewOptions() {

    class Builder {

        private var fieldName: String = ""
        private var isIconHidden: Boolean = false

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setIconHidden(isHidden: Boolean) = this.apply {
            this.isIconHidden = isHidden
        }

        fun build() = VGSCheckoutCVCOptions(fieldName, isIconHidden)
    }
}