package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutSetCardBrandsMode
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import com.verygoodsecurity.vgscheckout.util.extension.addAllWithReplace
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardNumberOptions private constructor(
    override val fieldName: String,
    val isIconHidden: Boolean,
    val cardBrands: Set<VGSCheckoutCardBrand>
) : ViewOptions() {

    class Builder {

        private var fieldName: String = ""
        private var isIconHidden: Boolean = false
        private var cardBrands: Set<VGSCheckoutCardBrand> = VGSCheckoutCardBrand.BRANDS

        fun setFieldName(fieldName: String) = this.apply {
            this.fieldName = fieldName
        }

        fun setIconHidden(isHidden: Boolean) = this.apply {
            this.isIconHidden = isHidden
        }

        fun setCardBrands(
            vararg brand: VGSCheckoutCardBrand,
            mode: VGSCheckoutSetCardBrandsMode = VGSCheckoutSetCardBrandsMode.MODIFY
        ) = this.apply {
            this.cardBrands = when (mode) {
                VGSCheckoutSetCardBrandsMode.MODIFY -> cardBrands.addAllWithReplace(*brand)
                VGSCheckoutSetCardBrandsMode.REPLACE -> setOf(*brand)
            }
        }

        fun build() = VGSCheckoutCardNumberOptions(fieldName, isIconHidden, cardBrands)
    }
}