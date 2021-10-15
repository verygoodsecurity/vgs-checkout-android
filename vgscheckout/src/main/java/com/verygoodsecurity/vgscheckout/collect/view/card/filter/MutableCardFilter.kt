package com.verygoodsecurity.vgscheckout.collect.view.card.filter

import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand

/** @suppress */
internal interface MutableCardFilter : VGSCardFilter {

    fun addCustomCardBrand(brand: CardBrand)

    fun setValidCardBrands(cardBrands: List<CardBrand>)
}