package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

internal class ResponseAction(
    val params:Map<String, Any>
): Action {

    override fun getAttributes(): MutableMap<String, Any> {
        return with(mutableMapOf<String, Any>()) {
            putAll(params)
            put(EVENT, SCAN)

            this
        }
    }

    companion object {
        private const val EVENT = "type"
        private const val SCAN = "Submit"
    }
}