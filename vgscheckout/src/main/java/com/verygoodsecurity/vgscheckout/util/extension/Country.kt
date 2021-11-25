package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.util.country.model.Country

internal fun Country.toVGSInfoRule() = VGSInfoRule.ValidationBuilder()
    .setRegex(postalCodeRegex)
    .build()