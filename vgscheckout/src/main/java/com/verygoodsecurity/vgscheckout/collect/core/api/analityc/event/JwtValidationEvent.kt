package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class JwtValidationEvent(val isSuccessful: Boolean) : Event(TYPE) {

    override val attributes: Map<String, Any> = LinkedHashMap<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessful) STATUS_OK else STATUS_FAILED)
    }

    companion object {

        private const val TYPE = "JWTValidation"
    }
}