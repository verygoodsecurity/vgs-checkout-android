package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.view.View
import androidx.annotation.IdRes
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

internal class InputViewStateHolder constructor(
    private val view: InputFieldView,
    private val listener: OnInputViewStateChangedListener
) : View.OnFocusChangeListener, InputFieldView.OnTextChangedListener, OnFieldStateChangeListener {

    private var state = ViewState()

    init {

        view.onFocusChangeListener = this
        view.addOnTextChangeListener(this)
        view.setOnFieldStateChangeListener(this)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) state = state.copy(hasFocusedBefore = true)
    }

    override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
        if (isEmpty.not() && state.isDirty.not()) state = state.copy(isDirty = true)
    }

    override fun onStateChange(state: FieldState) {
        this.state = this.state.copy(
            hasFocus = state.hasFocus,
            isValid = state.isValid,
            isEmpty = state.isEmpty
        )
        listener.onStateChange(view.id, this.state)
    }

    fun getState() = state
}

internal data class ViewState constructor(
    val hasFocus: Boolean = false,
    val hasFocusedBefore: Boolean = false,
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val isEmpty: Boolean = true
)

internal interface OnInputViewStateChangedListener {

    fun onStateChange(@IdRes id: Int, state: ViewState)
}