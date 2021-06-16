package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model

enum class VGSSetCardBrandsMode {

    /** Modify current card brands or/and add custom. */
    MODIFY,

    /** Replace current card brands with modified or/custom card brands. Only replaced brands will be detected as valid brands. */
    REPLACE
}