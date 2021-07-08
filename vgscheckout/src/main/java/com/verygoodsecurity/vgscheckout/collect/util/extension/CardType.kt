package com.verygoodsecurity.vgscheckout.collect.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.card.BrandParams
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType

/**
 * Map CardType to CardBrand.
 *
 * @return newly created CardBrand
 */
internal fun CardType.toCardBrand(): CardBrand {
    return CardBrand(
        this,
        this.regex,
        this.name,
        this.resId,
        BrandParams(
            this.mask,
            this.algorithm,
            this.rangeNumber,
            this.rangeCVV
        )
    )
}

/**
 * Map array CardTypes to list of CardBrands.
 *
 * @return list of newly created CardBrands.
 */
internal fun Array<CardType>.toCardBrands(): List<CardBrand> {
    return this.map { it.toCardBrand() }
}