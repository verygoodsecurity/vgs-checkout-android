package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Autofill(val fieldAnalyticsName: String) : Event(TYPE) {

    override val attributes: Map<String, Any> = mapOf(KEY_FIELD to fieldAnalyticsName)

    companion object {

        private const val TYPE = "Autofill"

        private const val KEY_FIELD = "field"
    }
}