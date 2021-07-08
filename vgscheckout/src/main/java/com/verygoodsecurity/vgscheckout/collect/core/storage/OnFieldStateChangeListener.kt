package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState


/**
 * Interface definition for a callback to be invoked when a view state is changed.
 *
 * @version 1.0.0
 */
internal interface OnFieldStateChangeListener {

    /**
     * Called when new changes is detected
     *
     * @param state current state of input field
     */
    fun onStateChange(state:FieldState)
}