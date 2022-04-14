package com.verygoodsecurity.vgscheckout.collect.view

import android.view.View
import com.verygoodsecurity.vgscheckout.collect.core.api.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.storage.DependencyListener

/** @suppress */
internal interface AccessibilityStatePreparer {
    fun getId(): Int
    fun getView(): View
    fun unsubscribe()
    fun getDependencyListener(): DependencyListener

    fun setAnalyticTracker(tr: AnalyticTracker?)
}