package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.view.VGSCheckoutTextInputLayout
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

internal fun InputFieldView.isInputEmpty() = getInnerState().isNullOrEmpty()

internal fun InputFieldView.isInputValid() = getInnerState().isValid

internal fun InputFieldView.setMaterialError(message: String?) {
    (parent as? VGSCheckoutTextInputLayout)?.error = message
}