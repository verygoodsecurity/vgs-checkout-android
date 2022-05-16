package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig

internal data class InitEvent(
    val type: ConfigType,
    val config: CheckoutConfig
) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_CONFIG_TYPE, type.name.lowercase())
        put(KEY_CONTENT, generateContent(config))
    }

    companion object {

        private const val TYPE = "Init"

        private const val KEY_CONFIG_TYPE = "config"
        private const val KEY_CONTENT = "content"
    }

    enum class ConfigType {

        CUSTOM,
        PAYOPT
    }
}