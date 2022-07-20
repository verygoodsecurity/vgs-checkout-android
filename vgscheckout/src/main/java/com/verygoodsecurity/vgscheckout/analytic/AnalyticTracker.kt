package com.verygoodsecurity.vgscheckout.analytic

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

internal interface AnalyticTracker {

    fun log(event: Event)
}