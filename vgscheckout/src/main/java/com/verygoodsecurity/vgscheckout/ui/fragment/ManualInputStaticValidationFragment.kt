package com.verygoodsecurity.vgscheckout.ui.fragment

import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseManualInputFragment
import com.verygoodsecurity.vgscheckout.util.extension.hideSoftKeyboard

internal class ManualInputStaticValidationFragment : BaseManualInputFragment() {

    override fun handleSaveClicked() {
        validate()
        updateErrors()
        requireActivity().hideSoftKeyboard()
        resolver.checkout(getInvalidInputAnalyticsNames())
    }
}