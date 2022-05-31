package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.view.doOnLayout
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType

internal abstract class ValidationManager constructor(
    private val context: Context,
    var country: Country?,
    val inputs: List<BaseInputField>
) {

    init {

        initTextChangeListener()
    }

    private fun initTextChangeListener() {
        inputs.forEach { input ->
            input.doOnLayout {
                input.doAfterTextChanged {
                    input.setMaterialError(null)
                }
            }
        }
    }

    /**
     * Validate all inputs and update error messages.
     *
     * @return list of invalid input fields.
     */
    fun validate(): List<BaseInputField> {
        val result = mutableListOf<BaseInputField>()
        inputs.filter { it.isShown }.forEach {
            val isValid = getValidationResult(it)
            if (!isValid) result.add(it)
        }
        return result
    }

    /**
     * Validate input field and update error message.
     *
     * @return true if field valid, false otherwise.
     */
    fun validate(input: BaseInputField): Boolean {
        if (!input.isShown) {
            return true
        }
        return getValidationResult(input)
    }

    private fun getValidationResult(input: BaseInputField): Boolean {
        val message = getErrorMessage(input)
        input.setMaterialError(message)
        return message.isNullOrEmpty()
    }

    private fun getErrorMessage(input: BaseInputField): String? = when {
        input.getFieldState().isNullOrEmpty() -> context.getString(getEmptyErrorMessage(input))
        !input.getFieldState().isValid -> context.getString(getInvalidErrorMessage(input))
        else -> null
    }

    // TODO("Use view ID after migration")
    @StringRes
    private fun getEmptyErrorMessage(input: BaseInputField): Int = when (input.getAnalyticsName()) {
        "cardHolder" -> R.string.vgs_checkout_card_holder_empty_error
        "cardNumber" -> R.string.vgs_checkout_card_number_empty_error
        "expDate" -> R.string.vgs_checkout_card_expiration_date_empty_error
        "cvc" -> R.string.vgs_checkout_security_code_empty_error
        "addressLine1" -> R.string.vgs_checkout_address_info_line1_empty_error
        "city" -> R.string.vgs_checkout_address_info_city_empty_error
        "postalCode" -> getPostalCodeEmptyErrorMessage()
        else -> R.string.empty
    }

    // TODO("Use view ID after migration")
    @StringRes
    private fun getInvalidErrorMessage(input: BaseInputField): Int =
        when (input.getAnalyticsName()) {
            "cardHolder" -> R.string.vgs_checkout_card_holder_invalid_error
            "cardNumber" -> R.string.vgs_checkout_card_number_invalid_error
            "expDate" -> R.string.vgs_checkout_card_expiration_date_invalid_error
            "cvc" -> R.string.vgs_checkout_security_code_invalid_error
            "postalCode" -> getPostalCodeInvalidErrorMessage()
            else -> R.string.empty
        }

    @StringRes
    private fun getPostalCodeEmptyErrorMessage() =
        when (country?.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zipcode_empty_error
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_empty_error
            else -> R.string.empty
        }

    @StringRes
    private fun getPostalCodeInvalidErrorMessage() =
        when (country?.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
            else -> R.string.empty
        }

    private fun BaseInputField.setMaterialError(message: String?) {
        ((parent as? FrameLayout)?.parent as? TextInputLayout)?.error = message
    }

    companion object {

        fun create(
            behaviour: VGSCheckoutFormValidationBehaviour,
            context: Context,
            country: Country,
            inputs: List<BaseInputField>
        ): ValidationManager = when (behaviour) {
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT -> StaticValidationManager(
                context,
                country,
                inputs
            )
            VGSCheckoutFormValidationBehaviour.ON_FOCUS -> DynamicValidationManager(
                context,
                country,
                inputs
            )
        }
    }
}