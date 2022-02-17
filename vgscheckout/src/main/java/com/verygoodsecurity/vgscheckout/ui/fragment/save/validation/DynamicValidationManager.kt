package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import android.view.View
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.util.country.model.Country

internal class DynamicValidationManager constructor(
    context: Context,
    country: Country,
    inputs: List<InputFieldView>
) : ValidationManager(context, country, inputs) {

    private val onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (v is InputFieldView && hasFocus.not() && v.isEdited()) {
            validate(v)
        }
    }

    init {

        initOnFocusChangeListener()
    }

    private fun initOnFocusChangeListener() {
        inputs.forEach {
            it.onFocusChangeListener = onFocusChangeListener
        }
    }
}