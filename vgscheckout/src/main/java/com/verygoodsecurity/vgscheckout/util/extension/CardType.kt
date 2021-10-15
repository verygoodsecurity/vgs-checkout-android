package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.card.CardType

internal fun String?.isAmericanExpress() = this == CardType.AMERICAN_EXPRESS.name
