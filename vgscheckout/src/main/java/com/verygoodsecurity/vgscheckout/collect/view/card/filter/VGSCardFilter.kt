package com.verygoodsecurity.vgscheckout.collect.view.card.filter

/** @suppress */
internal interface VGSCardFilter {

    fun detect(data: String?): CardBrandPreview
}