package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField

internal fun CVCInputField.setIsPreviewIconHidden(isHidden: Boolean) {
    setPreviewIconVisibility(
        if (isHidden)
            CVCInputField.PreviewIconVisibility.NEVER
        else {
            CVCInputField.PreviewIconVisibility.ALWAYS
        }
    )
}