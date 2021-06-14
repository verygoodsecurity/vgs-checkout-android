package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardNumberOptions private constructor(
    override val fieldName: String,
    val validCardBrands: Array<VGSCheckoutCardBrand>?
) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""
        private var validCardBrands: Array<VGSCheckoutCardBrand>? = null

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setValidCardBrands(vararg brand: VGSCheckoutCardBrand) = this.apply {
            this.validCardBrands = arrayOf(*brand)
        }

        fun build() = VGSCheckoutCardNumberOptions(fieldName, validCardBrands)
    }
}