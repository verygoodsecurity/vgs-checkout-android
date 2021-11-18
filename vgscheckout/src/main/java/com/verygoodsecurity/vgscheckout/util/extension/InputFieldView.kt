package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout

internal fun InputFieldView.isInputEmpty() = getFieldState()?.isEmpty == true

internal fun InputFieldView.isInputValid() = getFieldState()?.isValid == true

internal fun InputFieldView.setMaterialError(message: String?) {
    (this.parent as? VGSTextInputLayout)?.setError(message)
}