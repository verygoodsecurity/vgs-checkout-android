package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

internal data class HostnameValidation(
    val status: String,
    val hostname: String
) : Event(TYPE) {

    override val params: Map<String, Any> = mapOf()

    companion object {

        private const val TYPE = "HostNameValidation"
    }
}