package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

internal class ScanEvent(
    val params: Map<String, Any>
) : Event() {

    override fun getAttributes(): MutableMap<String, Any> {
        return with(mutableMapOf<String, Any>()) {
            putAll(params)
            put(EVENT, SCAN)

            this
        }
    }

    companion object {
        private const val EVENT = "type"
        private const val SCAN = "Scan"
    }
}