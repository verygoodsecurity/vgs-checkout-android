package com.verygoodsecurity.vgscheckout.view.checkout.adapter

import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.formatter.CardMaskAdapter

class CardMaskAdapter constructor(
    private val brands: Set<VGSCheckoutCardBrand>
) : CardMaskAdapter() {

    override fun getMask(cardType: CardType, name: String, bin: String, mask: String) =
        brands.find { it.name == name }?.mask ?: super.getMask(cardType, name, bin, mask)
}