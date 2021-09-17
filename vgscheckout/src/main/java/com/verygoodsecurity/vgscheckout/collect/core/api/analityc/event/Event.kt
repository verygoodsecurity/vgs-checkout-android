package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal abstract class Event constructor(type: String) {

    protected abstract val params: Map<String, Any>

    val payload: Map<String, Any> = mapOf(KEY_TYPE to type)
        get() = field + params

    companion object {

        private const val KEY_TYPE = "type"
    }
}