package com.verygoodsecurity.vgscheckout.ui.fragment.save.validation

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.StringRes
import androidx.core.view.doOnLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.isInputEmpty
import com.verygoodsecurity.vgscheckout.util.extension.isInputValid
import com.verygoodsecurity.vgscheckout.util.extension.setMaterialError

internal abstract class ValidationManager constructor(
    private val context: Context,
    var country: Country?,
    val inputs: List<InputFieldView>
) {

    init {

        initTextChangeListener()
    }

    private fun initTextChangeListener() {
        inputs.forEach {
            with(it) {
                doOnLayout {
                    addOnTextChangeListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun afterTextChanged(p0: Editable?) {
                            this@with.setMaterialError(null)
                        }
                    })
                }
            }
        }
    }

    /**
     * Validate all inputs and update error messages.
     *
     * @return list of invalid input fields.
     */
    fun validate(): List<InputFieldView> {
        val result = mutableListOf<InputFieldView>()
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

        return input.vgsParent?.run {
            getValidationResult(this)
        } ?: true
    }

    private fun getValidationResult(input: InputFieldView): Boolean {
        val message = getErrorMessage(input)
        input.setMaterialError(message)
        return message.isNullOrEmpty()
    }

    private fun getErrorMessage(input: InputFieldView): String? = when {
        input.isInputEmpty() -> context.getString(getEmptyErrorMessage(input))
        !input.isInputValid() -> context.getString(getInvalidErrorMessage(input))
        else -> null
    }

    @StringRes
    private fun getEmptyErrorMessage(input: InputFieldView): Int = when (input.id) {
        R.id.vgsEtCardHolder -> R.string.vgs_checkout_card_holder_empty_error
        R.id.vgsEtCardNumber -> R.string.vgs_checkout_card_number_empty_error
        R.id.vgsEtExpirationDate -> R.string.vgs_checkout_card_expiration_date_empty_error
        R.id.vgsEtSecurityCode -> R.string.vgs_checkout_security_code_empty_error
        R.id.vgsEtAddress -> R.string.vgs_checkout_address_info_line1_empty_error
        R.id.vgsEtCity -> R.string.vgs_checkout_address_info_city_empty_error
        R.id.vgsEtPostalCode -> getPostalCodeEmptyErrorMessage()
        else -> R.string.empty
    }

    @StringRes
    private fun getInvalidErrorMessage(input: InputFieldView): Int = when (input.id) {
        R.id.vgsEtCardHolder -> R.string.vgs_checkout_card_holder_invalid_error
        R.id.vgsEtCardNumber -> R.string.vgs_checkout_card_number_invalid_error
        R.id.vgsEtExpirationDate -> R.string.vgs_checkout_card_expiration_date_invalid_error
        R.id.vgsEtSecurityCode -> R.string.vgs_checkout_security_code_invalid_error
        R.id.vgsEtPostalCode -> getPostalCodeInvalidErrorMessage()
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

    companion object {

        fun create(
            behaviour: VGSCheckoutFormValidationBehaviour,
            context: Context,
            country: Country,
            inputs: List<InputFieldView>
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