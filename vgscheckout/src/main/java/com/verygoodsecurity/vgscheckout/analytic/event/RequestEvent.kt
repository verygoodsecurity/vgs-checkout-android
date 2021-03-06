package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig

internal data class RequestEvent(
    val isSuccessFull: Boolean,
    val invalidFieldTypes: List<String>,
    val config: CheckoutConfig
) : Event(TYPE, config) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessFull) STATUS_OK else STATUS_FAILED)
        if (invalidFieldTypes.isNotEmpty()) put(KEY_INVALID_FIELDS, invalidFieldTypes)
    }

    companion object {

        private const val TYPE = "BeforeSubmit"

        private const val KEY_INVALID_FIELDS = "fieldTypes"
    }
}