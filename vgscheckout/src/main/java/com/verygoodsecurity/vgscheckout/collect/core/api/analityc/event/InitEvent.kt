package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event

internal data class InitEvent(val type: ConfigType) : Event(TYPE) {

    override val attributes: Map<String, Any> = mapOf(KEY_CONFIG_TYPE to type.name.lowercase())

    companion object {

        private const val TYPE = "Init"

        private const val KEY_CONFIG_TYPE = "config"
    }

    enum class ConfigType {

        CUSTOM,
        PAYMENT_ORCHESTRATION
    }
}