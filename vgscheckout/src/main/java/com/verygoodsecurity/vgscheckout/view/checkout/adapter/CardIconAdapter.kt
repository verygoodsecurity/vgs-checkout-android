package com.verygoodsecurity.vgscheckout.view.checkout.adapter

import android.content.Context
import android.graphics.Rect
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.icon.CardIconAdapter

class CardIconAdapter constructor(
    context: Context,
    private val brands: Set<VGSCheckoutCardBrand>
) : CardIconAdapter(context) {

    override fun getIcon(cardType: CardType, name: String?, resId: Int, r: Rect) =
        brands.find { it.name == name }?.let { getDrawable(it.icon) }
            ?: super.getIcon(cardType, name, resId, r)
}