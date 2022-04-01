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
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.ResponseEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.isURLValid
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toVGSResponse
import com.verygoodsecurity.vgscheckout.collect.core.storage.InternalStorage
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasAccessNetworkStatePermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.hasInternetPermission
import com.verygoodsecurity.vgscheckout.collect.util.extension.isConnectionAvailable
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
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
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.binding.SaveCardViewBindingHelper
import com.verygoodsecurity.vgscheckout.ui.fragment.save.validation.ValidationManager
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.save.CardInfo
import com.verygoodsecurity.vgscheckout.util.command.save.SaveCardInfo
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.*

internal class SaveCardFragment : BaseFragment<CheckoutConfig>(),
    VGSCountryEditText.OnCountrySelectedListener, InputFieldView.OnEditorActionListener {

    private lateinit var binding: SaveCardViewBindingHelper
    private lateinit var validationHelper: ValidationManager

    private val inputFieldsStorage = InternalStorage()

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

    override fun onCountrySelected(country: Country) {
        validationHelper.country = country
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

    private fun initViews() {
        initCardDetailsViews()
        initBillingAddressViews()
        initSaveCardCheckbox()
        initSaveButton()
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
        with(config.formConfig.cardOptions) {
            initCardHolderView(cardHolderOptions)
            initCardNumberView(cardNumberOptions)
            initExpirationDateView(expirationDateOptions)
            initSecurityCodeView(cvcOptions)
        }
    }

    private fun initCardHolderView(options: CardHolderOptions) {
        if (!config.formConfig.isCardHolderVisible()) {
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
        binding.securityCodeEt.setOnEditorActionListener(this)
        binding.securityCodeEt.setImeOptions(
            if (config.formConfig.isBillingAddressVisible()) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_DONE
        )
        inputFieldsStorage.performSubscription(binding.securityCodeEt)
    }

    private fun initBillingAddressViews() {
        if (config.formConfig.isBillingAddressHidden()) {
            binding.billingAddressLL.gone()
            return
        }
        val rule = VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
        with(config.formConfig.addressOptions) {
            initCountryView(countryOptions)
            initAddressView(addressOptions, rule)
            initOptionalAddressView(optionalAddressOptions)
            initCityView(cityOptions, rule)
            initPostalCodeView(postalCodeOptions)
        }
    }

    private fun initCountryView(options: CountryOptions) {
        binding.countryEt.setFieldName(options.fieldName)
        binding.countryEt.setCountries(options.validCountries)
        binding.countryEt.onCountrySelectedListener = this
        inputFieldsStorage.performSubscription(binding.countryEt)
    }

    private fun initAddressView(options: AddressOptions, rule: VGSInfoRule) {
        binding.addressEt.setFieldName(options.fieldName)
        binding.addressEt.addRule(rule)
        inputFieldsStorage.performSubscription(binding.addressEt)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions) {
        binding.optionalAddressEt.setFieldName(options.fieldName)
        inputFieldsStorage.performSubscription(binding.optionalAddressEt)
    }

    private fun initCityView(options: CityOptions, rule: VGSInfoRule) {
        binding.cityEt.setFieldName(options.fieldName)
        binding.cityEt.addRule(rule)
        binding.cityEt.setOnEditorActionListener(this)
        inputFieldsStorage.performSubscription(binding.cityEt)
        updateCityView(binding.countryEt.selectedCountry)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        binding.postalCodeEt.setFieldName(options.fieldName)
        binding.postalCodeEt.setOnEditorActionListener(this)
        inputFieldsStorage.performSubscription(binding.postalCodeEt)
        updatePostalCodeView(binding.countryEt.selectedCountry)
    }

    private fun initSaveCardCheckbox() {
        if (config.formConfig.saveCardOptionEnabled) {
            with(binding.saveCardCheckbox) {
                resultBundle.putShouldSaveCard(isChecked)
                visible()
                setOnCheckedChangeListener { _, isChecked ->
                    resultBundle.putShouldSaveCard(isChecked)
                }
            }
        }
    }

    private fun initSaveButton() {
        binding.saveCardButton.text = title
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

    @StringRes
    private fun getPostalCodeHint(country: Country) =
        when (country.postalCodeType) {
            PostalCodeType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
            PostalCodeType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
            PostalCodeType.UNDEFINED -> R.string.empty
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
                    invalidFields
                )
            )
        }
    }

    private fun saveCard() {
        setIsLoading(true)
        with(config.getBaseUrl(requireContext())) {
            when {
                !isURLValid() -> onResponse(VGSError.URL_NOT_VALID.toVGSResponse())
                !requireActivity().hasInternetPermission() -> onResponse(VGSError.NO_INTERNET_PERMISSIONS.toVGSResponse())
                !requireActivity().hasAccessNetworkStatePermission() -> onResponse(VGSError.NO_NETWORK_CONNECTIONS.toVGSResponse())
                !requireActivity().isConnectionAvailable() -> onResponse(VGSError.NO_NETWORK_CONNECTIONS.toVGSResponse())
                else -> {
                    SaveCardInfo().execute(
                        CardInfo(
                            this,
                            config.routeConfig,
                            inputFieldsStorage.getAssociatedList()
                        )
                    ) {
                        when (it) {
                            is Result.Success -> onResponse(it.data)
                            is Result.Error -> finishWithResult(VGSCheckoutResult.Failed(resultBundle, it.e))
                        }
                    }
                }
            }
        }
    }

    private fun onResponse(response: VGSResponse) {
        if (!shouldHandleAddCard) {
            return
        }
        config.analyticTracker.log(
            ResponseEvent(
                response.code,
                response.latency,
                (this as? VGSResponse.ErrorResponse)?.message
            )
        )
        if (response.isNetworkConnectionError()) {
            setIsLoading(false)
            showNetworkError { saveCard() }
            return
        }
        handleAddCardResponse(response.toAddCardResponse())
    }

    private fun handleAddCardResponse(response: VGSCheckoutAddCardResponse) {
        resultBundle.putAddCardResponse(response)
        with(config) {
            if (response.isSuccessful && this is VGSCheckoutPaymentConfig) {
                try {
                    createTransaction(
                        response.getFinancialInstrumentId(),
                        paymentInfo.amount,
                        paymentInfo.currency,
                    )
                } catch (e: VGSCheckoutException) {
                    finishWithResult(VGSCheckoutResult.Failed(resultBundle, e))
                }
            } else {
                finishWithResult(resultBundle.toCheckoutResult(response.isSuccessful))
            }
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
    }
}