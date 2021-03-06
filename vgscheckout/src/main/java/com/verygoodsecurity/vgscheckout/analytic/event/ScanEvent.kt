package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal data class ScanEvent(
    val status: String,
    val scannerType: String,
    val scanId: String?
) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
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