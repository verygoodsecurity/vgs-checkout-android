package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import android.view.View
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.util.country.model.Country

internal class DynamicValidationManager constructor(
    context: Context,
    country: Country,
    inputs: List<InputFieldView>
) : ValidationManager(context, country, inputs), View.OnFocusChangeListener {

    init {

        initOnFocusChangeListener()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v is InputFieldView && hasFocus.not() && v.isEdited()) {
            validate(v)
        }
    }

    private fun initOnFocusChangeListener() {
        inputs.forEach {
            it.onFocusChangeListener = this
        }
    }
}