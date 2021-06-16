package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSSetCardBrandsMode
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import com.verygoodsecurity.vgscheckout.util.extension.addAllWithReplace
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardNumberOptions private constructor(
    override val fieldName: String,
    val cardBrands: Set<VGSCheckoutCardBrand>
) : ViewConfig() {

    class Builder {

        private var fieldName: String = ""
        private var cardBrands: Set<VGSCheckoutCardBrand> = VGSCheckoutCardBrand.BRANDS

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setCardBrands(
            vararg brand: VGSCheckoutCardBrand,
            mode: VGSSetCardBrandsMode = VGSSetCardBrandsMode.MODIFY
        ) = this.apply {
            this.cardBrands = when (mode) {
                VGSSetCardBrandsMode.MODIFY -> cardBrands.addAllWithReplace(*brand)
                VGSSetCardBrandsMode.REPLACE -> setOf(*brand)
            }
        }

        fun build() = VGSCheckoutCardNumberOptions(fieldName, cardBrands)
    }
}