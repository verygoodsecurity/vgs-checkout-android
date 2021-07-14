package com.verygoodsecurity.vgscheckout.view.checkout.card.adapter

import android.content.Context
import android.graphics.Rect
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.icon.CardIconAdapter

internal class CardIconAdapter constructor(
    context: Context,
    private val brands: Set<VGSCheckoutCardBrand>
) : CardIconAdapter(context) {

    override fun getIcon(cardType: CardType, name: String?, resId: Int, r: Rect) =
        brands.find { it.name == name }?.let { getDrawable(it.icon) }
            ?: super.getIcon(cardType, name, resId, r)
}