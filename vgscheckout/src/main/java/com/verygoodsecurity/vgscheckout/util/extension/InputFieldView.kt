package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

internal fun InputFieldView.isContentNotEmpty() = getFieldState()?.isEmpty == false