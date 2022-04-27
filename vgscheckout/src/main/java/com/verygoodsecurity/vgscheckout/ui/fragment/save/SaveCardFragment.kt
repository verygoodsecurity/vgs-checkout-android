package com.verygoodsecurity.vgscheckout.ui.fragment.save

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.analytic.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.storage.InternalStorage
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
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
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal class SaveCardFragment : BaseFragment<CheckoutConfig>(),
    VGSCountryEditText.OnCountrySelectedListener, InputFieldView.OnEditorActionListener {

    private lateinit var binding: SaveCardViewBindingHelper
    private lateinit var validationHelper: ValidationManager

    private val inputFieldsStorage = InternalStorage()
    private var addCardCommand: AddCardCommand? = null

    /**
     * TODO: Remove this flag and replace it with mocked view model in tests
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var shouldHandleAddCard: Boolean = true

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

    override fun onEditorAction(v: View?, actionId: Int, event: KeyEvent?): Boolean {
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
        binding.cardHolderEt.setFieldName(options.fieldName)
        inputFieldsStorage.performSubscription(binding.cardHolderEt)
    }

    private fun initCardNumberView(options: CardNumberOptions) {
        binding.cardNumberEt.setFieldName(options.fieldName)
        binding.cardNumberEt.setValidCardBrands(options.cardBrands)
        binding.cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        inputFieldsStorage.performSubscription(binding.cardNumberEt)
    }

    private fun initExpirationDateView(options: ExpirationDateOptions) {
        binding.expirationDateEt.setFieldName(options.fieldName)
        binding.expirationDateEt.setDateRegex(options.inputFormatRegex)
        binding.expirationDateEt.setOutputRegex(options.outputFormatRegex)
        binding.expirationDateEt.setSerializer(
            options.dateSeparateSerializer?.toCollectDateSeparateSerializer()
        )
        inputFieldsStorage.performSubscription(binding.expirationDateEt)
    }

    private fun initSecurityCodeView(options: CVCOptions) {
        binding.securityCodeEt.setFieldName(options.fieldName)
        binding.securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
        inputFieldsStorage.performSubscription(binding.securityCodeEt)
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
        binding.countryEt.setFieldName(options.fieldName)
        binding.countryEt.onCountrySelectedListener = this
        inputFieldsStorage.performSubscription(binding.countryEt)
    }

    private fun initAddressView(options: AddressOptions) {
        if (!options.isVisible()) {
            binding.addressTil.gone()
            return
        }
        binding.addressEt.setFieldName(options.fieldName)
        binding.addressEt.addRule(singleCharValidationRule)
        inputFieldsStorage.performSubscription(binding.addressEt)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions) {
        if (!options.isVisible()) {
            binding.optionalAddressTil.gone()
            return
        }
        binding.optionalAddressEt.setFieldName(options.fieldName)
        inputFieldsStorage.performSubscription(binding.optionalAddressEt)
    }

    private fun initCityView(options: CityOptions) {
        if (!options.isVisible()) {
            binding.cityTil.gone()
            binding.cityPostalAddressSpace.gone()
            return
        }
        binding.cityEt.setFieldName(options.fieldName)
        binding.cityEt.addRule(singleCharValidationRule)
        binding.cityEt.setOnEditorActionListener(this)
        inputFieldsStorage.performSubscription(binding.cityEt)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        if (!options.isVisible()) {
            binding.postalCodeTil.gone()
            binding.cityPostalAddressSpace.gone()
            return
        }
        binding.postalCodeEt.setFieldName(options.fieldName)
        binding.postalCodeEt.setOnEditorActionListener(this)
        inputFieldsStorage.performSubscription(binding.postalCodeEt)
        updatePostalCodeView(binding.countryEt.selectedCountry)
    }

    private fun updatePostalCodeView(country: Country) {
        if (country.isPostalCodeUndefined()) {
            inputFieldsStorage.unsubscribe(binding.postalCodeEt)
            binding.postalCodeEt.setText(null)
            binding.postalCodeEt.setIsRequired(false)
            binding.postalCodeTil.gone()
            binding.cityPostalAddressSpace.gone()
        } else {
            inputFieldsStorage.performSubscription(binding.postalCodeEt)
            binding.postalCodeEt.setIsRequired(true)
            binding.postalCodeEt.addRule(country.toVGSInfoRule())
            binding.postalCodeEt.resetText()
            binding.postalCodeTil.setHint(getString(getPostalCodeHint(country)))
            binding.postalCodeTil.setError(null)
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
            it.inputField?.setOnEditorActionListener(null)
            it.inputField?.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        }
        visitableInputs.lastOrNull()?.let {
            it.inputField?.setOnEditorActionListener(this@SaveCardFragment)
            it.inputField?.setImeOptions(EditorInfo.IME_ACTION_DONE)
        }
    }

    private fun handleSaveClicked() {
        requireActivity().hideSoftKeyboard()
        val invalidFields = validationHelper.validate()
        sendRequestEvent(invalidFields.map { it.getAnalyticsName() })
        if (invalidFields.isEmpty()) {
            saveCard()
        }
    }

    private fun sendRequestEvent(invalidFields: List<String> = emptyList()) {
        with(config) {
            analyticTracker.log(
                RequestEvent(
                    invalidFields.isEmpty(),
                    routeConfig.hostnamePolicy is VGSCheckoutHostnamePolicy.CustomHostname,
                    routeConfig.requestOptions.extraData.isNotEmpty(),
                    routeConfig.requestOptions.hasExtraHeaders,
                    formConfig.addressOptions.countryOptions.validCountries.isNotEmpty(),
                    routeConfig.requestOptions.mergePolicy,
                    formConfig.validationBehaviour,
                    invalidFields
                )
            )
        }
    }

    private fun saveCard() {
        setIsLoading(true)
        addCardCommand = AddCardCommand(requireContext())
        addCardCommand?.execute(
            AddCardCommand.Params(
                config.getBaseUrl(requireContext()),
                config.routeConfig.path,
                config.routeConfig,
                inputFieldsStorage.getAssociatedList()
            ),
            ::handleSaveCardResult
        )
    }

    private fun handleSaveCardResult(result: AddCardCommand.Result) {
        if (!shouldHandleAddCard) {
            return
        }
        logResponseEvent(result)
        logCreateFinInstrumentEvent(result)
        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setIsLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { saveCard() }
            return
        }
        with(resultHandler) {
            getResultBundle().putAddCardResponse(result.toCardResponse())
            if (config is VGSCheckoutAddCardConfig) getResultBundle().putIsPreSavedCard(false)
            setResult(result.isSuccessful)
        }
    }

    private fun logResponseEvent(result: AddCardCommand.Result) {
        config.analyticTracker.log(result.toResponseEvent())
    }

    private fun logCreateFinInstrumentEvent(result: AddCardCommand.Result) {
        if (config is VGSCheckoutAddCardConfig) {
            config.analyticTracker.log(
                FinInstrumentCrudEvent.create(
                    result.code,
                    result.isSuccessful,
                    result.message,
                    config is VGSCheckoutCustomConfig
                )
            )
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
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