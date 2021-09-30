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

    @JvmOverloads
    constructor(
        fieldName: String = "",
        isIconHidden: Boolean = false,
        vararg brand: VGSCheckoutCardBrand = emptyArray(),
        mode: VGSCheckoutSetCardBrandsMode = VGSCheckoutSetCardBrandsMode.MODIFY
    ) : this(
        fieldName,
        isIconHidden,
        when (mode) {
            VGSCheckoutSetCardBrandsMode.MODIFY -> VGSCheckoutCardBrand.BRANDS.addAllWithReplace(*brand)
            VGSCheckoutSetCardBrandsMode.REPLACE -> setOf(*brand)
        }
    )
}