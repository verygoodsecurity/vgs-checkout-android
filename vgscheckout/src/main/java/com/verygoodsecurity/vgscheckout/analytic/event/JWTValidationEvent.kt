package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal data class JWTValidationEvent(val isSuccessful: Boolean) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessful) STATUS_OK else STATUS_FAILED)
    }

    companion object {

        private const val TYPE = "JWTValidation"
    }
}