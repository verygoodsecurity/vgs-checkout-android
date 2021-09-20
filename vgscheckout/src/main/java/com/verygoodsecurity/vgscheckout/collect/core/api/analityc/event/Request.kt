package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Request constructor(val status: String, val fields: List<String>) :
    Event(TYPE, emptyMap()) {

    companion object {

        private const val TYPE = "BeforeSubmit"
    }
}