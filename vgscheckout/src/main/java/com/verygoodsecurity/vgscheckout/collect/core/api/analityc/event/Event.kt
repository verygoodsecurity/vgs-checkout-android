package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal abstract class Event {

    abstract val type: String

    abstract fun getAttributes(): MutableMap<String, Any>

    companion object {

        internal const val KEY_TYPE = "type"
    }
}