package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal class AddCardPaymentMethod constructor(
    val isPreSavedCard: Boolean,
    val isCustomConfig: Boolean
) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_PAYMENT_METHOD, if (isPreSavedCard) SAVED_CARD else NEW_CARD)
        put(KEY_CONFIG, if (isCustomConfig) CONFIG_CUSTOM else CONFIG_PAYOUT)
        put(KEY_CONFIG_TYPE, CONFIG_TYPE)
    }

    companion object {

        private const val TYPE = "AddCardPaymentMethod"

        private const val KEY_PAYMENT_METHOD = "paymentMethod"
        private const val KEY_CONFIG = "config"
        private const val KEY_CONFIG_TYPE = "configType"

        private const val SAVED_CARD = "savedCard"
        private const val NEW_CARD = "newCard"
        private const val CONFIG_CUSTOM = "custom"
        private const val CONFIG_PAYOUT = "payopt"
        private const val CONFIG_TYPE = "addCard"
    }
}