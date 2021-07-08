package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action.Action

interface AnalyticTracker {
    var isEnabled: Boolean
    fun logEvent(action: Action)
}