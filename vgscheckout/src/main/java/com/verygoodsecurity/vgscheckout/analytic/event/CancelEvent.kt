package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal class CancelEvent : Event(TYPE) {

    override val attributes: Map<String, Any> = emptyMap()

    companion object {

        private const val TYPE = "Cancel"
    }
}