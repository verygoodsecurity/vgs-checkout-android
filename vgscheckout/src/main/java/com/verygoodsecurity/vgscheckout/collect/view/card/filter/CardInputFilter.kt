package com.verygoodsecurity.vgscheckout.collect.view.card.filter

/** @suppress */
internal interface CardInputFilter {
    fun clearFilters()
    fun addFilter(filter: VGSCardFilter?)
}