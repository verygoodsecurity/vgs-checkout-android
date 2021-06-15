package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber

import com.verygoodsecurity.vgscheckout.PaymentCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardNumberOptions private constructor(
    override val fieldName: String,
    val validCardBrands: Array<VGSCheckoutCardBrand>?,
    val new_validCardBrands: Array<PaymentCardBrand>?
) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""
        private var validCardBrands: Array<VGSCheckoutCardBrand>? = null
        private var new_validCardBrands: Array<PaymentCardBrand>? = null

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setValidCardBrands(vararg brand: VGSCheckoutCardBrand) = this.apply {
            this.validCardBrands = arrayOf(*brand)
        }

        fun setValidCardBrands(vararg brand: PaymentCardBrand) = this.apply {
            this.new_validCardBrands = arrayOf(*brand)
        }

        fun build() = VGSCheckoutCardNumberOptions(fieldName, validCardBrands, new_validCardBrands)
    }
}