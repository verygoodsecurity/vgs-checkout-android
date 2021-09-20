package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal data class Autofill(
    val fieldAnalyticsName: String
) : Event(TYPE, mapOf(KEY_FIELD to fieldAnalyticsName)) {

    companion object {

        private const val TYPE = "Autofill"

        private const val KEY_FIELD = "field"
    }
}