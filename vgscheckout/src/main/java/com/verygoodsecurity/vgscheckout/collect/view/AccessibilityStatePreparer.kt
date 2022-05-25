package com.verygoodsecurity.vgscheckout.collect.view

import android.view.View
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker

/** @suppress */
internal interface AccessibilityStatePreparer {
    fun getId(): Int
    fun getView(): View
    fun unsubscribe()

    fun setAnalyticTracker(tr: AnalyticTracker?)
}