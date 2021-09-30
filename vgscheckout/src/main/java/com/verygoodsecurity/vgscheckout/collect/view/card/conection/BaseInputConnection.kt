package com.verygoodsecurity.vgscheckout.collect.view.card.conection

import com.verygoodsecurity.vgscheckout.collect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator

internal abstract class BaseInputConnection constructor(
    private val id: Int,
    internal var defaultValidator: VGSValidator?
) : InputRunnable {

    private var stateListeners = mutableListOf<OnVgsViewStateChangeListener>()

    protected fun isValid(input: String?): Boolean {
        return defaultValidator?.isValid(input) ?: false
    }

    protected fun clearAllListeners() {
        stateListeners.clear()
    }

    protected fun addNewListener(listener: OnVgsViewStateChangeListener) {
        if (!stateListeners.contains(listener)) {
            stateListeners.add(listener)
            run()
        }
    }

    protected fun notifyAllListeners(output: VGSFieldState) {
        stateListeners.forEach { it.emit(id, output) }
    }
}