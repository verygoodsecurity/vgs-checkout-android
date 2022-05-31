package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.util.country.model.Country

internal class StaticValidationManager constructor(
    context: Context,
    country: Country,
    inputs: List<BaseInputField>
) : ValidationManager(context, country, inputs)