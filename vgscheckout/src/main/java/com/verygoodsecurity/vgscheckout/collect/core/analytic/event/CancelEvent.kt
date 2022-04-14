package com.verygoodsecurity.vgscheckout.collect.core.analytic.event

import com.verygoodsecurity.vgscheckout.collect.core.analytic.event.core.Event

private const val TYPE = "Cancel"

internal object CancelEvent : Event(TYPE) {

    override val attributes: Map<String, Any> = emptyMap()
}