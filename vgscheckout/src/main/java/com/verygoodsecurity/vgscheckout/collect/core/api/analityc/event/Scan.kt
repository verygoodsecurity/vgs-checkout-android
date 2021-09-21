package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Scan(
    val status: String,
    val scannerType: String,
    val scanId: String?
) : Event(TYPE) {

    override val attributes: Map<String, Any> = LinkedHashMap<String, Any>().apply {
        put(KEY_STATUS, status)
        put(KEY_SCANNER_TYPE, scannerType)
        scanId?.let { put(KEY_SCAN_ID, scanId) }
    }

    companion object {

        private const val TYPE = "Scan"

        private const val KEY_SCANNER_TYPE = "scannerType"
        private const val KEY_SCAN_ID = "scanId"
    }
}