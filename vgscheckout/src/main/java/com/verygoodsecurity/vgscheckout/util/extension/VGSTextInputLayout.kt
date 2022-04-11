package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout

internal val VGSTextInputLayout.inputField: InputFieldView?
    get() = getChildAt(0) as? InputFieldView