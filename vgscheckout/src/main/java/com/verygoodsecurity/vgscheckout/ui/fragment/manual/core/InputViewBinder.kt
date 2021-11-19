package com.verygoodsecurity.vgscheckout.ui.fragment.manual.core

import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

internal interface InputViewBinder {

    fun bind(vararg view: InputFieldView)

    fun unbind(vararg view: InputFieldView)
}