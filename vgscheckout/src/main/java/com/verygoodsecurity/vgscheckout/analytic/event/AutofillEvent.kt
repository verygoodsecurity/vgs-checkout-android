package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal data class AutofillEvent(val fieldAnalyticsName: String) : Event(TYPE) {

    override val attributes: Map<String, Any> = mapOf(KEY_FIELD to fieldAnalyticsName)

    companion object {

        private const val TYPE = "Autofill"

        private const val KEY_FIELD = "field"
    }
}