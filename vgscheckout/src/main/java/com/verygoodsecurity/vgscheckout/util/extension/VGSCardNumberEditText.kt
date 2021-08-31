package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand

internal fun VGSCardNumberEditText.setValidCardBrands(brands: Set<VGSCheckoutCardBrand>) {
    setValidCardBrands(brands.map { it.toCollectCardBrand() })
}

internal fun VGSCardNumberEditText.setIsCardBrandPreviewHidden(isHidden: Boolean) {
    setCardBrandPreviewIconMode(
        if (isHidden)
            CardInputField.PreviewIconMode.NEVER
        else
            CardInputField.PreviewIconMode.ALWAYS
    )
}