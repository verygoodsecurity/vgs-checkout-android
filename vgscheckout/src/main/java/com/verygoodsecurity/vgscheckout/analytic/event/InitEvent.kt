package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig

internal data class InitEvent(
    val type: ConfigType,
    val config: CheckoutConfig
) : Event(TYPE, config) {

    override val attributes: Map<String, Any> =
        mutableMapOf(KEY_CONFIG_TYPE to type.name.lowercase())

    companion object {

        private const val TYPE = "Init"

        private const val KEY_CONFIG_TYPE = "config"
    }

    enum class ConfigType {

        CUSTOM,
        PAYOPT
    }
}