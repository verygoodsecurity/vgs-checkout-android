package com.verygoodsecurity.vgscheckout.config.ui.view.address.address

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutOptionalAddressOptions private constructor(
    override val fieldName: String
) : ViewOptions() {

    class Builder {

        private var fieldName: String = ""

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun build() = VGSCheckoutOptionalAddressOptions(fieldName)
    }
}