package com.verygoodsecurity.vgscheckout.collect.view.card.conection

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardInputFilter

/** @suppress */
internal interface InputRunnable:Runnable, CardInputFilter {
    fun setOutput(state: VGSFieldState)
    fun getOutput(): VGSFieldState
}