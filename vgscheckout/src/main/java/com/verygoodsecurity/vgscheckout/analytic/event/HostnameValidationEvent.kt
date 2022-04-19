package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal data class HostnameValidationEvent(
    val isSuccessful: Boolean,
    val hostname: String
) : Event(TYPE) {

    override val attributes: Map<String, Any> = LinkedHashMap<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessful) STATUS_OK else STATUS_FAILED)
        put(KEY_HOSTNAME, hostname)
    }

    companion object {

        private const val KEY_HOSTNAME = "hostname"

        private const val TYPE = "HostNameValidation"
    }
}