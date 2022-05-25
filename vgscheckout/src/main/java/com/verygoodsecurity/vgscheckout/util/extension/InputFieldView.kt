package com.verygoodsecurity.vgscheckout.util.extension

import androidx.core.view.doOnLayout
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout

internal fun InputFieldView.isInputEmpty() = getInnerState().isNullOrEmpty()

internal fun InputFieldView.isInputValid() = getInnerState().isValid

internal fun InputFieldView.setMaterialError(message: String?) {
    (parent as? VGSTextInputLayout)?.setError(message)
}

internal fun InputFieldView.addOnTextChangeListenerOnLayout(listener: InputFieldView.OnTextChangedListener) {
    doOnLayout {
        addOnTextChangeListener(listener)
    }
}