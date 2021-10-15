package com.verygoodsecurity.vgscheckout.collect.view.material.internal

import com.verygoodsecurity.vgscheckout.collect.view.FieldState

/** @suppress */
internal interface InputLayoutState:FieldState {
    fun restore(textInputLayoutWrapper: TextInputLayoutWrapper?)
}