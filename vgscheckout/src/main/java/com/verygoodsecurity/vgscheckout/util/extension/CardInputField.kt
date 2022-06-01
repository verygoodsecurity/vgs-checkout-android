package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand

internal fun CardInputField.setValidCardBrands(brands: Set<VGSCheckoutCardBrand>) {
    setValidCardBrands(brands.map { it.toCollectCardBrand() })
}

internal fun CardInputField.setIsCardBrandPreviewHidden(isHidden: Boolean) {
    setPreviewIconMode(
        if (isHidden)
            CardInputField.PreviewIconMode.NEVER.ordinal
        else
            CardInputField.PreviewIconMode.ALWAYS.ordinal
    )
}