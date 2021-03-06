package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal data class ResponseEvent(
    val code: Int,
    val errorMsg: String?,
    val latency: Long
) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_CODE, code)
        put(KEY_LATENCY, latency)
        errorMsg?.let { put(KEY_ERROR_MSG, errorMsg) }
    }

    companion object {

        private const val TYPE = "Submit"

        private const val KEY_CODE = "statusCode"
        private const val KEY_LATENCY = "latency"
        private const val KEY_ERROR_MSG = "error"
    }
}