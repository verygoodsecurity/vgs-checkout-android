package com.verygoodsecurity.vgscheckout.collect.core.api.analytic.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analytic.event.core.Event

internal data class ResponseEvent(
    val code: Int,
    val latency: Long,
    val errorMsg: String?
) : Event(TYPE) {

    override val attributes: Map<String, Any> = LinkedHashMap<String, Any>().apply {
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