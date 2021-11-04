package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutSetCardBrandsMode
import com.verygoodsecurity.vgscheckout.util.extension.addAllWithReplace
import kotlinx.parcelize.Parcelize

/**
 * Custom flow card number input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param isIconHidden defines if card brand icon should be hidden.
 * @param cardBrands list of brands that can be detected.
 */
@Parcelize
class VGSCheckoutCustomCardNumberOptions private constructor(
    override val fieldName: String,
    override val isIconHidden: Boolean,
    override val cardBrands: Set<VGSCheckoutCardBrand>,
) : CardNumberOptions() {

    /**
     * Public constructor. Allow to specify set card brands mode.
     *
     * @param fieldName text to be used for data transfer to VGS proxy.
     * @param isIconHidden defines if card brand icon should be hidden.
     * @param brand list of brands that can be detected. Does not allow duplicates.
     * @param mode defines if [brand] list should override default brands list or need to be merged with it.
     */
    @JvmOverloads
    constructor(
        fieldName: String = "",
        isIconHidden: Boolean = false,
        vararg brand: VGSCheckoutCardBrand = emptyArray(),
        mode: VGSCheckoutSetCardBrandsMode = VGSCheckoutSetCardBrandsMode.MODIFY,
    ) : this(
        fieldName,
        isIconHidden,
        when (mode) {
            VGSCheckoutSetCardBrandsMode.MODIFY -> VGSCheckoutCardBrand.BRANDS.addAllWithReplace(*brand)
            VGSCheckoutSetCardBrandsMode.REPLACE -> setOf(*brand)
        }
    )
}