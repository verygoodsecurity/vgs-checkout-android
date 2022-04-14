package com.verygoodsecurity.vgscheckout.collect.core.api.analytic

import com.verygoodsecurity.vgscheckout.collect.core.api.analytic.event.core.Event

internal interface AnalyticTracker {

    var isEnabled: Boolean

    fun log(event: Event)
}