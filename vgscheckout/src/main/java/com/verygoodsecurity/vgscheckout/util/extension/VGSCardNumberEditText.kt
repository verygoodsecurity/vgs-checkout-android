package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText

internal fun VGSCardNumberEditText.isAmericanExpress() =
    getState()?.cardBrand == CardType.AMERICAN_EXPRESS.name
