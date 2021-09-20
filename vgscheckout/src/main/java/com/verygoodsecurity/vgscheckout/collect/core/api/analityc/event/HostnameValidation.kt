package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class HostnameValidation(
    val status: String,
    val hostname: String
) : Event(TYPE, mapOf(KEY_STATUS to status, KEY_HOSTNAME to hostname)) {

    companion object {

        private const val KEY_STATUS = "status"
        private const val KEY_HOSTNAME = "hostname"

        private const val TYPE = "HostNameValidation"
    }
}