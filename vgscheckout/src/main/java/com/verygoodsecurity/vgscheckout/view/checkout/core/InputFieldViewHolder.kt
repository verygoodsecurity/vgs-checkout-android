package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.view.View
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

internal class InputFieldViewHolder constructor(
    val title: MaterialTextView,
    val input: InputFieldView,
    val listener: OnInputFieldStateChanged? = null
) : View.OnFocusChangeListener, InputFieldView.OnTextChangedListener, OnFieldStateChangeListener {

    var state = ViewState()
        private set

    init {

        input.onFocusChangeListener = this
        input.addOnTextChangeListener(this)
        input.setOnFieldStateChangeListener(this)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) state = state.copy(hasBeenFocusedBefore = true)
    }

    override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
        if (!isEmpty && !state.isDirty) state = state.copy(isDirty = true)
    }

    override fun onStateChange(state: FieldState) {
        this.state = this.state.copy(
            hasFocus = state.hasFocus,
            isValid = state.isValid,
            isEmpty = state.isEmpty
        )
        listener?.onStateChange(this.state)
    }

    internal data class ViewState constructor(
        val hasFocus: Boolean = false,
        val hasBeenFocusedBefore: Boolean = false,
        val isDirty: Boolean = false,
        val isValid: Boolean = false,
        val isEmpty: Boolean = true
    )

    internal interface OnInputFieldStateChanged {

        fun onStateChange(state: ViewState)
    }
}