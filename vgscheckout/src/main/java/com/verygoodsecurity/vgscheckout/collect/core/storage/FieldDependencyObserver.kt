package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState

/** @suppress */
internal interface FieldDependencyObserver {
    fun onRefreshState(state: VGSFieldState)
    fun onStateUpdate(state: VGSFieldState)
}