package com.verygoodsecurity.vgscheckout.ui.fragment.save.core

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
import com.verygoodsecurity.vgscheckout.ui.core.InputViewBinder
import com.verygoodsecurity.vgscheckout.ui.core.ValidationResultListener
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardDynamicValidationFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardStaticValidationFragment
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.*

@Suppress("MemberVisibilityCanBePrivate")
internal abstract class BaseSaveCardFragment : Fragment(), LoadingHandler,
    InputFieldView.OnTextChangedListener, VGSCountryEditText.OnCountrySelectedListener,
    InputFieldView.OnEditorActionListener {

    protected val formConfig: CheckoutFormConfig by lazy { requireParcelable(KEY_BUNDLE_CONFIG) }
    protected val buttonTitle: String by lazy { requireString(KEY_BUNDLE_BUTTON_TITLE) }

    protected lateinit var binding: SaveCardViewBindingHelper
    protected lateinit var inputViewBinder: InputViewBinder
    protected lateinit var validationListener: ValidationResultListener

    private val validationRequiredInputs: List<InputFieldView> by lazy {
        mutableListOf(
            binding.cardNumberEt,
            binding.expirationDateEt,
            binding.securityCodeEt,
        ).apply {
            if (formConfig.isCardHolderVisible()) add(binding.cardHolderEt)
            if (formConfig.isBillingAddressVisible()) {
                add(binding.addressEt)
                add(binding.cityEt)
                add(binding.postalCodeEt)
            }
        }
    }

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
        binding = SaveCardViewBindingHelper(
            inflater,
            R.layout.vgs_checkout_save_card_fragment
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
        setViewsEnabled(!isLoading)
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
        initCardDetailsViews()
        initBillingAddressViews()
        initSaveCardCheckbox()
        initSaveButton()
    }

    open fun handleSaveClicked() {
        requireActivity().hideSoftKeyboard()
        val invalidFields = validate()
        if (invalidFields.isEmpty()) {
            val shouldSaveCard = if (!formConfig.saveCardOptionEnabled) null else binding.saveCardCheckbox.isChecked
            validationListener.onSuccess(shouldSaveCard)
        } else {
            validationListener.onFailed(invalidFields.map { it.getAnalyticsName() })
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
        if (formConfig.isCardHolderHidden()) {
            binding.cardHolderTil.gone()
            return
        }
        binding.cardHolderEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.cardHolderEt)
        binding.cardHolderEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initCardNumberView(options: CardNumberOptions) {
        binding.cardNumberEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.cardNumberEt)
        binding.cardNumberEt.setValidCardBrands(options.cardBrands)
        binding.cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        binding.cardNumberEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initExpirationDateView(options: ExpirationDateOptions) {
        binding.expirationDateEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.expirationDateEt)
        binding.expirationDateEt.setDateRegex(options.inputFormatRegex)
        binding.expirationDateEt.setOutputRegex(options.outputFormatRegex)
        binding.expirationDateEt.setSerializer(
            options.dateSeparateSerializer?.toCollectDateSeparateSerializer()
        )
        binding.expirationDateEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initSecurityCodeView(options: CVCOptions) {
        binding.securityCodeEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.securityCodeEt)
        binding.securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
        binding.securityCodeEt.setOnEditorActionListener(this)
        binding.securityCodeEt.setImeOptions(
            if (formConfig.isBillingAddressVisible()) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_DONE
        )
        binding.securityCodeEt.addOnTextChangeListenerOnLayout(this)
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
        inputViewBinder.bind(binding.countryEt)
        binding.countryEt.setCountries(options.validCountries)
        binding.countryEt.onCountrySelectedListener = this
    }

    private fun initAddressView(options: AddressOptions, rule: VGSInfoRule) {
        binding.addressEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.addressEt)
        binding.addressEt.addRule(rule)
        binding.addressEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions, rule: VGSInfoRule) {
        binding.optionalAddressEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.optionalAddressEt)
        binding.optionalAddressEt.addRule(rule)
    }

    private fun initCityView(options: CityOptions, rule: VGSInfoRule) {
        binding.cityEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.cityEt)
        binding.cityEt.addRule(rule)
        binding.cityEt.setOnEditorActionListener(this)
        updateCityView(binding.countryEt.selectedCountry)
        binding.cityEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        binding.postalCodeEt.setFieldName(options.fieldName)
        inputViewBinder.bind(binding.postalCodeEt)
        binding.postalCodeEt.setOnEditorActionListener(this)
        updatePostalCodeView(binding.countryEt.selectedCountry)
        binding.postalCodeEt.addOnTextChangeListenerOnLayout(this)
    }

    private fun initSaveCardCheckbox() {
        binding.saveCardCheckbox.setVisible(formConfig.saveCardOptionEnabled)
    }

    private fun initSaveButton() {
        binding.saveCardButton.text = buttonTitle
        binding.saveCardButton.setOnClickListener { handleSaveClicked() }
    }

    private fun updateCityView(country: Country) {
        binding.cityEt.setImeOptions(
            if (country.isPostalCodeUndefined()) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
        )
    }

    private fun updatePostalCodeView(country: Country) {
        if (country.isPostalCodeUndefined()) {
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

    private fun unbindAllViews() {
        with(binding) {
            inputViewBinder.unbind(
                cardHolderEt,
                cardNumberEt,
                expirationDateEt,
                securityCodeEt,
                countryEt,
                addressEt,
                optionalAddressEt,
                cityEt,
                postalCodeEt
            )
        }
    }

    @StringRes
    private fun getPostalCodeHint(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
            PostalCodeType.UNDEFINED -> R.string.empty
        }

    /**
     * Validate all inputs and update error messages.
     *
     * @return list of invalid input fields.
     */
    protected fun validate(): List<InputFieldView> {
        val result = mutableListOf<InputFieldView>()
        validationRequiredInputs.forEach {
            val isValid = validate(it)
            if (!isValid) result.add(it)
        }
        return result
    }

    /**
     * Validate input field and update error message.
     *
     * @return true if field valid, false otherwise.
     */
    protected fun validate(input: InputFieldView): Boolean {
        val message = getErrorMessage(input)
        input.setMaterialError(message)
        return message.isNullOrEmpty()
    }

    private fun clearError(input: InputFieldView) {
        input.setMaterialError(null)
    }

    private fun getErrorMessage(input: InputFieldView): String? = when {
        input.isInputEmpty() -> getString(getEmptyErrorMessage(input))
        !input.isInputValid() -> getString(getInvalidErrorMessage(input))
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
            PostalCodeType.UNDEFINED -> R.string.empty
        }

    @StringRes
    private fun getPostalCodeInvalidErrorMessage(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
            PostalCodeType.UNDEFINED -> R.string.empty
        }

    private fun setViewsEnabled(isEnabled: Boolean) {
        binding.cardDetailsLL.setEnabled(isEnabled, true, binding.cardDetailsMtv)
        binding.billingAddressLL.setEnabled(isEnabled, true, binding.billingAddressMtv)
        val alpha = if (isEnabled) ICON_ALPHA_ENABLED else ICON_ALPHA_DISABLED
        binding.cardNumberEt.setDrawablesAlphaColorFilter(alpha)
        binding.securityCodeEt.setDrawablesAlphaColorFilter(alpha)
        binding.saveCardCheckbox.isEnabled = isEnabled
    }

    private fun setSaveButtonIsLoading(isLoading: Boolean) {
        with(binding.saveCardButton) {
            isClickable = !isLoading
            if (isLoading) {
                text = getString(R.string.vgs_checkout_button_processing_title)
                icon = getDrawableCompat(R.drawable.vgs_checkout_ic_loading_animated_white_16)
                (icon as? Animatable)?.start()
            } else {
                text = buttonTitle
                icon = null
            }
        }
    }

    companion object {

        internal const val TAG = "BaseSaveCardFragment"

        private const val ICON_ALPHA_ENABLED = 1f
        private const val ICON_ALPHA_DISABLED = 0.5f
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
        private const val KEY_BUNDLE_CONFIG = "key_bundle_from_config"
        private const val KEY_BUNDLE_BUTTON_TITLE = "key_bundle_button_title"

        fun create(formConfig: CheckoutFormConfig, buttonTitle: String): BaseSaveCardFragment {
            val bundle = Bundle().apply {
                putParcelable(KEY_BUNDLE_CONFIG, formConfig)
                putString(KEY_BUNDLE_BUTTON_TITLE, buttonTitle)
            }
            val fragment = when (formConfig.validationBehaviour) {
                VGSCheckoutFormValidationBehaviour.ON_SUBMIT -> SaveCardStaticValidationFragment()
                VGSCheckoutFormValidationBehaviour.ON_FOCUS -> SaveCardDynamicValidationFragment()
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}