package com.verygoodsecurity.vgscheckout.collect.core

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState

/** @suppress */
internal interface OnVgsViewStateChangeListener {
    fun emit(viewId:Int, state: VGSFieldState)
}