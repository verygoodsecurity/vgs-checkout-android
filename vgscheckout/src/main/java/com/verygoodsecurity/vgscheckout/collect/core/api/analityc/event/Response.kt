package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class Response(
    val status: String,
    val code: Int,
    val latency: Long,
    val errorMsg: String?
) : Event(
    TYPE,
    mutableMapOf(
        KEY_STATUS to status,
        KEY_CODE to code,
        KEY_LATENCY to latency
    ).also { map ->
        errorMsg?.let { map[KEY_ERROR_MSG] = it }
    }
) {

    companion object {

        private const val TYPE = "Submit"

        private const val KEY_STATUS = "status"
        private const val KEY_CODE = "statusCode"
        private const val KEY_LATENCY = "latency"
        private const val KEY_ERROR_MSG = "error"
    }
}