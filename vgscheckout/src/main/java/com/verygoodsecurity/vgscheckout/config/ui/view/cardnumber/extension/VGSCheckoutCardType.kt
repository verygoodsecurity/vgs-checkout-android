package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.extension

import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutBrandParams
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardType

/**
 * Map CardType to CardBrand.
 *
 * @return newly created CardBrand
 */
fun VGSCheckoutCardType.toCardBrand(): VGSCheckoutCardBrand {
    return VGSCheckoutCardBrand(
        this.regex,
        this.name,
        this.resId,
        VGSCheckoutBrandParams(
            this.mask,
            this.algorithm,
            this.rangeNumber,
            this.rangeCVV
        ),
        this
    )
}

/**
 * Map array CardTypes to list of CardBrands.
 *
 * @return list of newly created CardBrands.
 */
fun Array<VGSCheckoutCardType>.toCardBrands(): List<VGSCheckoutCardBrand> {
    return this.map { it.toCardBrand() }
}