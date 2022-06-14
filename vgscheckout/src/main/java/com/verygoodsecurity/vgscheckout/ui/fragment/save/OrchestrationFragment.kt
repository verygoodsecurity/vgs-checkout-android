package com.verygoodsecurity.vgscheckout.ui.fragment.save

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.analytic.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.util.extension.mapToAssociatedList
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.view.date.DatePickerMode
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.CountryInputField
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.core.OrchestrationConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.CardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.CardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.CVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.ExpirationDateOptions
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.binding.SaveCardViewBindingHelper
import com.verygoodsecurity.vgscheckout.ui.fragment.save.validation.ValidationManager
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.cardHolderOptions
import com.verygoodsecurity.vgscheckout.util.extension.cardNumberOptions
import com.verygoodsecurity.vgscheckout.util.extension.expiryOptions
import com.verygoodsecurity.vgscheckout.util.extension.postalCodeOptions
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal abstract class OrchestrationFragment<T : CheckoutConfig> : BaseFragment<T>(),
    CountryInputField.OnCountrySelectedListener, TextView.OnEditorActionListener {

    /**
     * TODO: Remove this flag and replace it with mocked view model in tests
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal var shouldHandleAddCard: Boolean = true

    private lateinit var binding: SaveCardViewBindingHelper
    private lateinit var validationHelper: ValidationManager

    private var addCardCommand: AddCardCommand? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = SaveCardViewBindingHelper(
            inflater,
            R.layout.vgs_checkout_save_card_fragment
        )
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initValidationHelper()
    }

    override fun onDestroy() {
        super.onDestroy()
        addCardCommand?.cancel()
    }

    override fun onCountrySelected(country: Country) {
        validationHelper.country = country
        if (config.postalCodeOptions.isVisible()) updatePostalCodeView(country)
        setImeActionDoneToLastVisibleInput()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            handleSaveClicked()
            return true
        }
        return false
    }

    private fun initViews() {
        initCardDetailsViews()
        initBillingAddressViews()
        initSaveCardCheckbox()
        initSaveButton()
        setImeActionDoneToLastVisibleInput()
    }

    private fun initValidationHelper() {
        validationHelper = ValidationManager.create(
            config.formConfig.validationBehaviour,
            requireContext(),
            binding.countryEt.selectedCountry,
            mutableListOf(
                binding.cardHolderEt,
                binding.cardNumberEt,
                binding.expirationDateEt,
                binding.securityCodeEt,
                binding.addressEt,
                binding.cityEt,
                binding.postalCodeEt
            )
        )
    }

    private fun initCardDetailsViews() {
        initCardHolderView(config.cardHolderOptions)
        initCardNumberView(config.cardNumberOptions)
        initExpirationDateView(config.expiryOptions)
        initSecurityCodeView(config.cvcOptions)
    }

    private fun initCardHolderView(options: CardHolderOptions) {
        if (!options.isVisible()) {
            binding.cardHolderTil.gone()
            return
        }
        binding.cardHolderEt.tag = options.fieldName
        binding.cardHolderEt.setAnalyticsName("cardHolder")
    }

    private fun initCardNumberView(options: CardNumberOptions) {
        binding.cardNumberEt.tag = options.fieldName
        binding.cardNumberEt.setAnalyticsName("cardNumber")
        binding.cardNumberEt.setValidCardBrands(options.cardBrands)
        binding.cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        binding.cardNumberEt.setCardPreviewIconGravity(Gravity.START)
        binding.cardNumberEt.setNumberDivider(" ")
        binding.cardNumberEt.dependentField = binding.securityCodeEt
    }

    private fun initExpirationDateView(options: ExpirationDateOptions) {
        binding.expirationDateEt.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_DATETIME_VARIATION_DATE
        binding.expirationDateEt.tag = options.fieldName
        binding.expirationDateEt.setAnalyticsName("expDate")
        binding.expirationDateEt.setDatePickerMode(DatePickerMode.INPUT.ordinal)
        binding.expirationDateEt.setDatePattern(options.inputFormatRegex)
        binding.expirationDateEt.setOutputPattern(options.outputFormatRegex)
        binding.expirationDateEt.setFieldDataSerializer(options.dateSeparateSerializer?.toCollectDateSeparateSerializer())
    }

    private fun initSecurityCodeView(options: CVCOptions) {
        binding.securityCodeEt.tag = options.fieldName
        binding.securityCodeEt.setAnalyticsName("cvc")
        binding.securityCodeEt.setPreviewIconGravity(CVCInputField.PreviewIconGravity.START)
        binding.securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
    }

    private fun initBillingAddressViews() {
        if (!config.billingAddressOptions.isVisible()) {
            binding.billingAddressLL.gone()
            return
        }
        initCountryView(config.countryOptions)
        initAddressView(config.addressOptions)
        initOptionalAddressView(config.optionalAddressOptions)
        initCityView(config.cityOptions)
        initPostalCodeView(config.postalCodeOptions)
        if (isBillingAddressInputGone()) binding.billingAddressLL.gone()
    }

    private fun isBillingAddressInputGone(): Boolean {
        return with(binding) { countryTil.isGone && addressTil.isGone && optionalAddressTil.isGone && cityTil.isGone && postalCodeTil.isGone }
    }

    private fun initCountryView(options: CountryOptions) {
        binding.countryEt.setCountries(options.validCountries)
        val isVisible = options.isVisible()
        binding.countryTil.isVisible = isVisible
        if (!isVisible) {
            if (config is VGSCheckoutCustomConfig) {
                return
            } else {
                VGSCheckoutLogger.warn(message = "Country field is hidden in billing address. You should provide validCountries array.")
            }
        }
        binding.countryEt.tag = options.fieldName
        binding.countryEt.setAnalyticsName("country")
        binding.countryEt.onCountrySelectedListener = this
    }

    private fun initAddressView(options: AddressOptions) {
        if (!options.isVisible()) {
            binding.addressTil.gone()
            return
        }
        binding.addressEt.tag = options.fieldName
        binding.addressEt.setAnalyticsName("addressLine1")
        binding.addressEt.addRule(singleCharValidationRule)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions) {
        if (!options.isVisible()) {
            binding.optionalAddressTil.gone()
            return
        }
        binding.optionalAddressEt.tag = options.fieldName
        binding.optionalAddressEt.setAnalyticsName("addressLine2")
    }

    private fun initCityView(options: CityOptions) {
        if (!options.isVisible()) {
            binding.cityTil.gone()
            binding.cityPostalAddressSpace.gone()
            return
        }
        binding.cityEt.tag = options.fieldName
        binding.cityEt.setAnalyticsName("city")
        binding.cityEt.addRule(singleCharValidationRule)
        binding.cityEt.setOnEditorActionListener(this)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        if (!options.isVisible()) {
            binding.postalCodeTil.gone()
            binding.cityPostalAddressSpace.gone()
            return
        }
        binding.postalCodeEt.tag = options.fieldName
        binding.postalCodeEt.setAnalyticsName("postalCode")
        binding.postalCodeEt.setOnEditorActionListener(this)
        updatePostalCodeView(binding.countryEt.selectedCountry)
    }

    private fun updatePostalCodeView(country: Country) {
        if (country.isPostalCodeUndefined()) {
            binding.postalCodeEt.text = null
            binding.postalCodeEt.setIsRequired(false)
            binding.postalCodeTil.gone()
            binding.cityPostalAddressSpace.gone()
        } else {
            binding.postalCodeEt.setIsRequired(true)
            binding.postalCodeEt.addRule(country.toVGSInfoRule())
            binding.postalCodeEt.resetText()
            binding.postalCodeTil.hint = getString(getPostalCodeHint(country))
            binding.postalCodeTil.error = null
            binding.postalCodeTil.visible()
            binding.cityPostalAddressSpace.isVisible = binding.cityTil.isVisible
        }
    }

    @StringRes
    private fun getPostalCodeHint(country: Country) = when (country.postalCodeType) {
        PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
        PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
        PostalCodeType.UNDEFINED -> R.string.empty
    }

    private fun initSaveCardCheckbox() {
        if (config.formConfig.saveCardOptionEnabled) {
            with(binding.saveCardCheckbox) {
                resultHandler.getResultBundle().putShouldSaveCard(isChecked)
                visible()
                setOnCheckedChangeListener { _, isChecked ->
                    resultHandler.getResultBundle().putShouldSaveCard(isChecked)
                }
            }
        }
    }

    private fun initSaveButton() {
        binding.saveCardButton.text = title
        binding.saveCardButton.setOnClickListener { handleSaveClicked() }
    }

    private fun setImeActionDoneToLastVisibleInput() {
        val visitableInputs = listOf(
            binding.securityCodeTil,
            binding.addressTil,
            binding.optionalAddressTil,
            binding.cityTil,
            binding.postalCodeTil
        ).filter { it.isVisible }
        visitableInputs.forEach {
            it.editText?.setOnEditorActionListener(null)
            it.editText?.imeOptions = EditorInfo.IME_ACTION_NEXT
        }
        visitableInputs.lastOrNull()?.let {
            it.editText?.setOnEditorActionListener(this@OrchestrationFragment)
            it.editText?.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    protected fun handleSaveClicked() {
        binding.getStates().mapToAssociatedList()

        requireActivity().hideSoftKeyboard()
        val invalidFields = validationHelper.validate()
        sendRequestEvent(invalidFields.map { it.getAnalyticsName() })
        if (invalidFields.isEmpty()) {
            processUserCard()
        }
    }

    private fun processUserCard() {
        setLoading(true)
        addCardCommand = AddCardCommand(
            requireContext(),
            AddCardCommand.Params(
                config.getBaseUrl(requireContext()),
                config.routeConfig.path,
                config.routeConfig,
                getStates()
            )
        )
        addCardCommand?.execute(::processSaveCardResult)
    }

    private fun processSaveCardResult(result: AddCardCommand.Result) {
        if (!shouldHandleAddCard) {
            return
        }
        logSaveCardResponse(result)

        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { handleSaveClicked() }
            return
        }

        with(resultHandler) {
            getResultBundle().putAddCardResponse(result.toCardResponse())
            if (config is OrchestrationConfig) getResultBundle().putIsPreSavedCard(false)
        }

        handleSaveCardResult(result)
    }

    protected abstract fun handleSaveCardResult(result: AddCardCommand.Result)

    private fun logSaveCardResponse(result: AddCardCommand.Result) {
        //todo: check if we have to send these events with PaymentConfig
        config.analyticTracker.log(result.toResponseEvent())
        config.analyticTracker.log(
            FinInstrumentCrudEvent.create(
                result.code,
                result.isSuccessful,
                result.message,
                config is VGSCheckoutCustomConfig
            )
        )
    }

    private fun sendRequestEvent(invalidFields: List<String> = emptyList()) {
        with(config) {
            analyticTracker.log(
                RequestEvent(
                    invalidFields.isEmpty(),
                    invalidFields,
                    config
                )
            )
        }
    }

    protected fun getStates() = binding.getStates().mapToAssociatedList()

    protected fun setLoading(isLoading: Boolean) {
        setViewsEnabled(!isLoading)
        setSaveButtonIsLoading(isLoading)
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
                text = title
                icon = null
            }
        }
    }

    companion object {

        private const val ICON_ALPHA_ENABLED = 1f
        private const val ICON_ALPHA_DISABLED = 0.5f
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1

        private val singleCharValidationRule = VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
    }
}