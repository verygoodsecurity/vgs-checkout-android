package com.verygoodsecurity.vgscheckout.ui.fragment.manual.core

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.CardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.CardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.CVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.ExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.manual.ManualInputDynamicValidationFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.manual.ManualInputStaticValidationFragment
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.*

@Suppress("MemberVisibilityCanBePrivate")
internal abstract class BaseManualInputFragment : Fragment(), LoadingHandler,
    InputFieldView.OnTextChangedListener, VGSCountryEditText.OnCountrySelectedListener,
    InputFieldView.OnEditorActionListener {

    protected val formConfig: CheckoutFormConfig by lazy { requireArgument(KEY_BUNDLE_CONFIG) }
    protected val inputFieldErrors = mutableMapOf<InputFieldView, String>()

    protected lateinit var binding: ManualInputViewBindingHelper
    protected lateinit var inputViewBinder: InputViewBinder
    protected lateinit var validationListener: ValidationResultListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputViewBinder = requireActivity() as InputViewBinder
        validationListener = requireActivity() as ValidationResultListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ManualInputViewBindingHelper(
            inflater,
            R.layout.vgs_checkout_manual_input_fragment
        )
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindAllViews()
    }

    override fun setIsLoading(isLoading: Boolean) {
        setInputViewsEnabled(!isLoading)
        setSaveButtonIsLoading(isLoading)
    }

    override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
        clearError(view)
    }

    override fun onCountrySelected(country: Country) {
        updatePostalCodeView(country)
        updateCityView(country)
    }

    override fun onEditorAction(v: View?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            handleSaveClicked()
            return true
        }
        return false
    }

    @CallSuper
    protected open fun initViews(view: View) {
        initBillingAddressViews()
        initCardDetailsViews()
        bindAllViews()
        initSaveButton()
    }

    open fun handleSaveClicked() {
        requireActivity().hideSoftKeyboard()
        validate()
        updateErrors()
        if (inputFieldErrors.filter { it.value.isNotEmpty() }.isEmpty()) {
            validationListener.onSuccess()
        } else {
            validationListener.onFailed(getInvalidInputAnalyticsNames())
        }
    }

    private fun initCardDetailsViews() {
        with(formConfig.cardOptions) {
            initCardHolderView(cardHolderOptions)
            initCardNumberView(cardNumberOptions)
            initExpirationDateView(expirationDateOptions)
            initSecurityCodeView(cvcOptions)
        }
    }

    private fun initCardHolderView(options: CardHolderOptions) {
        if (options.visibility == VGSCheckoutFieldVisibility.HIDDEN) {
            binding.cardHolderTil.gone()
            return
        }
        binding.cardHolderEt.setFieldName(options.fieldName)
        binding.cardHolderEt.addOnTextChangeListener(this)
    }

    private fun initCardNumberView(options: CardNumberOptions) {
        binding.cardNumberEt.setFieldName(options.fieldName)
        binding.cardNumberEt.setValidCardBrands(options.cardBrands)
        binding.cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        binding.cardNumberEt.addOnTextChangeListener(this)
    }

    private fun initExpirationDateView(options: ExpirationDateOptions) {
        binding.expirationDateEt.setFieldName(options.fieldName)
        binding.expirationDateEt.setDateRegex(options.inputFormatRegex)
        binding.expirationDateEt.setOutputRegex(options.outputFormatRegex)
        binding.expirationDateEt.setSerializer(
            options.dateSeparateSerializer?.toCollectDateSeparateSerializer()
        )
        binding.expirationDateEt.addOnTextChangeListener(this)
    }

    private fun initSecurityCodeView(options: CVCOptions) {
        binding.securityCodeEt.setFieldName(options.fieldName)
        binding.securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
        binding.securityCodeEt.addOnTextChangeListener(this)
    }

    private fun initBillingAddressViews() {
        if (formConfig.isBillingAddressHidden()) {
            binding.billingAddressLL.gone()
            return
        }
        val validationRule = VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
        with(formConfig.addressOptions) {
            initCountryView(countryOptions)
            initAddressView(addressOptions, validationRule)
            initOptionalAddressView(optionalAddressOptions, validationRule)
            initCityView(cityOptions, validationRule)
            initPostalCodeView(postalCodeOptions)
        }
    }

    private fun initCountryView(options: CountryOptions) {
        binding.countryEt.setFieldName(options.fieldName)
        binding.countryEt.setCountries(options.validCountries)
        binding.countryEt.onCountrySelectedListener = this
    }

    private fun initAddressView(options: AddressOptions, rule: VGSInfoRule) {
        binding.addressEt.setFieldName(options.fieldName)
        binding.addressEt.addRule(rule)
        binding.addressEt.addOnTextChangeListener(this)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions, rule: VGSInfoRule) {
        binding.optionalAddressEt.setFieldName(options.fieldName)
        binding.optionalAddressEt.addRule(rule)
    }

    private fun initCityView(options: CityOptions, rule: VGSInfoRule) {
        binding.cityEt.setFieldName(options.fieldName)
        binding.cityEt.addRule(rule)
        binding.cityEt.addOnTextChangeListener(this)
        binding.cityEt.setOnEditorActionListener(this)
        updateCityView(binding.countryEt.selectedCountry)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        binding.postalCodeEt.setFieldName(options.fieldName)
        binding.postalCodeEt.addOnTextChangeListener(this)
        binding.postalCodeEt.setOnEditorActionListener(this)
        updatePostalCodeView(binding.countryEt.selectedCountry)
    }

    private fun initSaveButton() {
        binding.saveCardButton.setOnClickListener { handleSaveClicked() }
    }

    private fun updateCityView(country: Country) {
        if (country.postalCodeType == PostalCodeType.NOTHING) {
            binding.cityEt.setImeOptions(EditorInfo.IME_ACTION_DONE)
        } else {
            binding.cityEt.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        }
    }

    private fun updatePostalCodeView(country: Country) {
        if (country.postalCodeType == PostalCodeType.NOTHING) {
            binding.postalCodeEt.setText(null)
            binding.postalCodeEt.setIsRequired(false)
            binding.postalCodeTil.gone()
        } else {
            binding.postalCodeEt.setIsRequired(true)
            binding.postalCodeEt.addRule(country.toVGSInfoRule())
            binding.postalCodeEt.resetText()
            binding.postalCodeTil.setHint(getString(getPostalCodeHint(country)))
            binding.postalCodeTil.setError(null)
            binding.postalCodeTil.visible()
        }
    }

    private fun bindAllViews() {
        with(binding) {
            inputViewBinder.bind(cardHolderEt,
                cardNumberEt,
                expirationDateEt,
                securityCodeEt,
                countryEt,
                addressEt,
                optionalAddressEt,
                cityEt,
                postalCodeEt)
        }
    }

    private fun unbindAllViews() {
        with(binding) {
            inputViewBinder.unbind(cardHolderEt,
                cardNumberEt,
                expirationDateEt,
                securityCodeEt,
                countryEt,
                addressEt,
                optionalAddressEt,
                cityEt,
                postalCodeEt)
        }
    }

    @StringRes
    private fun getPostalCodeHint(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
            PostalCodeType.NOTHING -> R.string.empty
        }

    protected fun validate(
        vararg input: InputFieldView = arrayOf(
            binding.cardHolderEt,
            binding.cardNumberEt,
            binding.expirationDateEt,
            binding.securityCodeEt,
            binding.addressEt,
            binding.cityEt,
            binding.postalCodeEt
        ),
    ) {
        input.forEach { validate(it) }
    }

    protected fun validate(input: InputFieldView) {
        inputFieldErrors[input] = getErrorMessage(input)
    }

    protected fun updateErrors() {
        inputFieldErrors.forEach {
            it.key.setMaterialError(it.value)
        }
    }

    protected fun updateError(input: InputFieldView) {
        input.setMaterialError(inputFieldErrors[input])
    }

    protected fun clearError(input: InputFieldView) {
        inputFieldErrors[input] = ""
        updateError(input)
    }

    private fun getErrorMessage(
        input: InputFieldView,
    ): String = getString(when {
        input.isInputEmpty() -> getEmptyErrorMessage(input)
        !input.isInputValid() -> getInvalidErrorMessage(input)
        else -> R.string.empty
    })

    @StringRes
    private fun getEmptyErrorMessage(input: InputFieldView): Int = when (input.id) {
        R.id.vgsEtCardHolder -> R.string.vgs_checkout_card_holder_empty_error
        R.id.vgsEtCardNumber -> R.string.vgs_checkout_card_number_empty_error
        R.id.vgsEtExpirationDate -> R.string.vgs_checkout_card_expiration_date_empty_error
        R.id.vgsEtSecurityCode -> R.string.vgs_checkout_security_code_empty_error
        R.id.vgsEtAddress -> R.string.vgs_checkout_address_info_line1_empty_error
        R.id.vgsEtCity -> R.string.vgs_checkout_address_info_city_empty_error
        R.id.vgsEtPostalCode -> getPostalCodeEmptyErrorMessage(binding.countryEt.selectedCountry)
        else -> R.string.empty
    }

    @StringRes
    private fun getInvalidErrorMessage(input: InputFieldView): Int = when (input.id) {
        R.id.vgsEtCardHolder -> R.string.vgs_checkout_card_holder_invalid_error
        R.id.vgsEtCardNumber -> R.string.vgs_checkout_card_number_invalid_error
        R.id.vgsEtExpirationDate -> R.string.vgs_checkout_card_expiration_date_invalid_error
        R.id.vgsEtSecurityCode -> R.string.vgs_checkout_security_code_invalid_error
        R.id.vgsEtPostalCode -> getPostalCodeInvalidErrorMessage(binding.countryEt.selectedCountry)
        else -> R.string.empty
    }

    @StringRes
    private fun getPostalCodeEmptyErrorMessage(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zipcode_empty_error
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_empty_error
            PostalCodeType.NOTHING -> R.string.empty
        }

    @StringRes
    private fun getPostalCodeInvalidErrorMessage(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
            PostalCodeType.NOTHING -> R.string.empty
        }

    private fun getInvalidInputAnalyticsNames(): List<String> =
        inputFieldErrors.map { if (it.value.isEmpty()) null else it.key.getAnalyticsName() }
            .filterNotNull()

    private fun setInputViewsEnabled(isEnabled: Boolean) {
        binding.cardDetailsLL.setEnabled(isEnabled, true, binding.cardDetailsMtv)
        binding.billingAddressLL.setEnabled(isEnabled, true, binding.billingAddressMtv)
        val alpha = if (isEnabled) ICON_ALPHA_ENABLED else ICON_ALPHA_DISABLED
        binding.cardNumberEt.setDrawablesAlphaColorFilter(alpha)
        binding.securityCodeEt.setDrawablesAlphaColorFilter(alpha)
    }

    private fun setSaveButtonIsLoading(isLoading: Boolean) {
        with(binding.saveCardButton) {
            isClickable = !isLoading
            if (isLoading) {
                text = getString(R.string.vgs_checkout_save_button_processing_title)
                icon = getDrawableCompat(R.drawable.vgs_checkout_ic_loading_animated_white_16)
                (icon as? Animatable)?.start()
            } else {
                text = getString(R.string.vgs_checkout_save_button_save_card_title)
                icon = null
            }
        }
    }

    companion object {

        private const val ICON_ALPHA_ENABLED = 1f
        private const val ICON_ALPHA_DISABLED = 0.5f
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
        private const val KEY_BUNDLE_CONFIG = "key_bundle_from_config"

        fun create(formConfig: CheckoutFormConfig): BaseManualInputFragment {
            val bundle = Bundle().apply { putParcelable(KEY_BUNDLE_CONFIG, formConfig) }
            val fragment = when (formConfig.validationBehaviour) {
                VGSCheckoutFormValidationBehaviour.ON_SUBMIT -> ManualInputStaticValidationFragment()
                VGSCheckoutFormValidationBehaviour.ON_FOCUS -> ManualInputDynamicValidationFragment()
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}