package com.verygoodsecurity.vgscheckout.collect.core

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState

/** @suppress */
interface OnVgsViewStateChangeListener {
    fun emit(viewId:Int, state: VGSFieldState)
}