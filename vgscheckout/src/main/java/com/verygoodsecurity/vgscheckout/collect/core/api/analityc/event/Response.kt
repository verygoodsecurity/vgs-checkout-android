package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal data class Response(val params: Map<String, Any>) : Event() {

    override val type: String = TYPE

    override fun getAttributes(): MutableMap<String, Any> {
        return with(mutableMapOf<String, Any>()) {
            putAll(params)
            put(KEY_TYPE, type)
            this
        }
    }

    companion object {

        private const val TYPE = "Submit"
    }
}