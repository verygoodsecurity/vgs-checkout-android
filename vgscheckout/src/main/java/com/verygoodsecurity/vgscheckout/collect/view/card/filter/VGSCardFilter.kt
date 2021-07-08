package com.verygoodsecurity.vgscheckout.collect.view.card.filter

/** @suppress */
interface VGSCardFilter {

    fun detect(data: String?): CardBrandPreview
}