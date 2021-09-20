package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Request(
    val status: String,
    val invalidFields: List<String>?
) : Event(
    TYPE,
    emptyMap()
) {

    companion object {

        private const val TYPE = "BeforeSubmit"
    }
}