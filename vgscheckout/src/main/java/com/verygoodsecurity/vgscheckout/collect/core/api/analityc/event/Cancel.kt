package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

private const val TYPE = "Cancel"

internal object Cancel : Event(TYPE) {

    override val attributes: Map<String, Any> = emptyMap()
}