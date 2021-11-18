package com.verygoodsecurity.vgscheckout.ui.core

import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

// TODO: Think about naming
internal interface CheckoutResolver {

    fun bind(vararg view: InputFieldView)

    fun unbind(vararg view: InputFieldView)

    fun checkout(invalidFields: List<String>)
}