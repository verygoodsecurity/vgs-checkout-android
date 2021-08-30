package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText

internal fun CardVerificationCodeEditText.setIsPreviewIconHidden(isHidden: Boolean) {
    setPreviewIconMode(
        if (isHidden)
            CVCInputField.PreviewIconVisibility.NEVER
        else {
            CVCInputField.PreviewIconVisibility.ALWAYS
        }
    )
}