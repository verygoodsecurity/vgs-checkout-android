package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.Event

internal interface AnalyticTracker {
    var isEnabled: Boolean
    fun log(event: Event)
}