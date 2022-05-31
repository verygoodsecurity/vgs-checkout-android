package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.util.country.model.Country

internal class DynamicValidationManager constructor(
    context: Context,
    country: Country,
    inputs: List<BaseInputField>
) : ValidationManager(context, country, inputs) {

    private val stateListener = object : BaseInputField.OnFieldStateChangeListener {
        override fun onStateChange(inputField: BaseInputField, state: FieldState) {
            if (!state.hasFocus && inputField.isEdited) validate(inputField)
        }
    }

    init {
        initOnFieldStateChangeListener()
    }

    private fun initOnFieldStateChangeListener() {
        inputs.forEach {
            it.setOnFieldStateChangeListener(stateListener)
        }
    }
}