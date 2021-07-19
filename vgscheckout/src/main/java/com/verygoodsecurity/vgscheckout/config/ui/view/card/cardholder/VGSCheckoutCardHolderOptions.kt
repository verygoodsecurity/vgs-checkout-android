package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardHolderOptions private constructor(
    override val fieldName: String,
    val visibility: VGSCheckoutFieldVisibility
) : ViewOptions() {

    class Builder {

        private var fieldName: String = ""
        private var visibility = VGSCheckoutFieldVisibility.VISIBLE

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setVisibility(visibility: VGSCheckoutFieldVisibility) = this.apply {
            this.visibility = visibility
        }

        fun build() = VGSCheckoutCardHolderOptions(fieldName, visibility)
    }
}