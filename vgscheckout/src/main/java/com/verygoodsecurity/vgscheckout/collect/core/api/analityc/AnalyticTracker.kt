package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action.Event

internal interface AnalyticTracker {
    var isEnabled: Boolean
    fun logEvent(event: Event)
}