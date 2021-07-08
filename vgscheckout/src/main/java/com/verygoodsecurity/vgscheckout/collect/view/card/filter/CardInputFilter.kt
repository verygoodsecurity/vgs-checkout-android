package com.verygoodsecurity.vgscheckout.collect.view.card.filter

/** @suppress */
interface CardInputFilter {
    fun clearFilters()
    fun addFilter(filter: VGSCardFilter?)
}