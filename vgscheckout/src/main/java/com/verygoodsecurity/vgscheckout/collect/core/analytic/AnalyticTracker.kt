package com.verygoodsecurity.vgscheckout.collect.core.analytic

import com.verygoodsecurity.vgscheckout.collect.core.analytic.event.core.Event

internal interface AnalyticTracker {

    var isEnabled: Boolean

    fun log(event: Event)
}