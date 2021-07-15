package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model

enum class VGSCheckoutSetCardBrandsMode {

    /** Modify current card brands or/and add custom. */
    MODIFY,

    /** Replace current card brands with modified or/custom card brands. Only replaced brands will be detected as valid brands. */
    REPLACE
}