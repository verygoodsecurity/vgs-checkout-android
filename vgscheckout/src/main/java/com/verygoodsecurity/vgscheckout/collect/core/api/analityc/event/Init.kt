package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal class Init : Event(TYPE) {

    override val params: Map<String, Any> = emptyMap()

    companion object {

        private const val TYPE = "Init"
    }
}