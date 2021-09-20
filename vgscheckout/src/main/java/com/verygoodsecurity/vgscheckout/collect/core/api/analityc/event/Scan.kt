package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Scan(
    val status: String,
    val scannerType: String,
    val scanId: String?
) : Event(
    TYPE,
    mutableMapOf(
        KEY_STATUS to status,
        KEY_SCANNER_TYPE to scannerType,
    ).also { map ->
        scanId?.let { map[KEY_SCAN_ID] = it }
    }
) {

    companion object {

        private const val TYPE = "Scan"

        private const val KEY_STATUS = "status"
        private const val KEY_SCANNER_TYPE = "scannerType"
        private const val KEY_SCAN_ID = "scanId"
    }
}