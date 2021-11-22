package com.verygoodsecurity.vgscheckout.ui.fragment.manual

import android.view.View
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.ui.fragment.manual.core.BaseManualInputFragment

internal class ManualInputDynamicValidationFragment : BaseManualInputFragment(),
    View.OnFocusChangeListener {

    override fun initViews(view: View) {
        super.initViews(view)
        initOnFocusChangeListener()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v is InputFieldView && hasFocus.not() && v.isEdited()) {
            validate(v)
            updateError(v)
        }
    }

    private fun initOnFocusChangeListener() {
        binding.cardHolderEt.onFocusChangeListener = this
        binding.cardNumberEt.onFocusChangeListener = this
        binding.expirationDateEt.onFocusChangeListener = this
        binding.securityCodeEt.onFocusChangeListener = this
        binding.addressEt.onFocusChangeListener = this
        binding.cityEt.onFocusChangeListener = this
        binding.postalCodeEt.onFocusChangeListener = this
    }
}