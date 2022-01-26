package com.verygoodsecurity.vgscheckout.ui.fragment.save

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
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
import com.verygoodsecurity.vgscheckout.ui.core.OnAddCardResponseListener
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.binding.SaveCardViewBindingHelper
import com.verygoodsecurity.vgscheckout.ui.fragment.save.validation.ValidationManager
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalCodeType
import com.verygoodsecurity.vgscheckout.util.extension.*

internal class SaveCardFragment : BaseFragment<CheckoutConfig>(), VgsCollectResponseListener,
    VGSCountryEditText.OnCountrySelectedListener, InputFieldView.OnEditorActionListener {

    private lateinit var binding: SaveCardViewBindingHelper
    private lateinit var validationHelper: ValidationManager
    private lateinit var responseListener: OnAddCardResponseListener

    private val collect: VGSCollect by lazy {
        CollectProvider().get(requireContext(), config).apply {
            addOnResponseListeners(this@SaveCardFragment)
        }
    }

    override fun getToolbarTitle(): String = getString(
        when (config) {
            is VGSCheckoutCustomConfig -> R.string.vgs_checkout_add_card_title
            else -> R.string.vgs_checkout_new_card_title
        }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        responseListener = activity as OnAddCardResponseListener
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        collect.onDestroy()
    }

    override fun onResponse(response: VGSResponse) {
        if (response.isNetworkConnectionError()) {
            setIsLoading(false)
            errorHandler.showNetworkError { saveCard() }
            return
        }
        val addCardResponse = response.toAddCardResponse()
        val shouldSave =
            if (!config.formConfig.saveCardOptionEnabled) null else binding.saveCardCheckbox.isChecked
        responseListener.onAddCardResponse(addCardResponse, shouldSave)
    }

    override fun setIsLoading(isLoading: Boolean) {
        setViewsEnabled(!isLoading)
        setSaveButtonIsLoading(isLoading)
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
        collect.bindView(binding.cardHolderEt)
    }

    private fun initCardNumberView(options: CardNumberOptions) {
        binding.cardNumberEt.setFieldName(options.fieldName)
        collect.bindView(binding.cardNumberEt)
        binding.cardNumberEt.setValidCardBrands(options.cardBrands)
        binding.cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
    }

    private fun initExpirationDateView(options: ExpirationDateOptions) {
        binding.expirationDateEt.setFieldName(options.fieldName)
        collect.bindView(binding.expirationDateEt)
        binding.expirationDateEt.setDateRegex(options.inputFormatRegex)
        binding.expirationDateEt.setOutputRegex(options.outputFormatRegex)
        binding.expirationDateEt.setSerializer(
            options.dateSeparateSerializer?.toCollectDateSeparateSerializer()
        )
    }

    private fun initSecurityCodeView(options: CVCOptions) {
        binding.securityCodeEt.setFieldName(options.fieldName)
        collect.bindView(binding.securityCodeEt)
        binding.securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
        binding.securityCodeEt.setOnEditorActionListener(this)
        binding.securityCodeEt.setImeOptions(
            if (config.formConfig.isBillingAddressVisible()) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_DONE
        )
    }

    private fun initBillingAddressViews() {
        if (config.formConfig.isBillingAddressHidden()) {
            binding.billingAddressLL.gone()
            return
        }
        val validationRule = VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
        with(config.formConfig.addressOptions) {
            initCountryView(countryOptions)
            initAddressView(addressOptions, validationRule)
            initOptionalAddressView(optionalAddressOptions, validationRule)
            initCityView(cityOptions, validationRule)
            initPostalCodeView(postalCodeOptions)
        }
    }

    private fun initCountryView(options: CountryOptions) {
        binding.countryEt.setFieldName(options.fieldName)
        collect.bindView(binding.countryEt)
        binding.countryEt.setCountries(options.validCountries)
        binding.countryEt.onCountrySelectedListener = this
    }

    private fun initAddressView(options: AddressOptions, rule: VGSInfoRule) {
        binding.addressEt.setFieldName(options.fieldName)
        collect.bindView(binding.addressEt)
        binding.addressEt.addRule(rule)
    }

    private fun initOptionalAddressView(options: OptionalAddressOptions, rule: VGSInfoRule) {
        binding.optionalAddressEt.setFieldName(options.fieldName)
        collect.bindView(binding.optionalAddressEt)
        binding.optionalAddressEt.addRule(rule)
    }

    private fun initCityView(options: CityOptions, rule: VGSInfoRule) {
        binding.cityEt.setFieldName(options.fieldName)
        collect.bindView(binding.cityEt)
        binding.cityEt.addRule(rule)
        binding.cityEt.setOnEditorActionListener(this)
        updateCityView(binding.countryEt.selectedCountry)
    }

    private fun initPostalCodeView(options: PostalCodeOptions) {
        binding.postalCodeEt.setFieldName(options.fieldName)
        collect.bindView(binding.postalCodeEt)
        binding.postalCodeEt.setOnEditorActionListener(this)
        updatePostalCodeView(binding.countryEt.selectedCountry)
    }

    private fun initSaveCardCheckbox() {
        binding.saveCardCheckbox.setVisible(config.formConfig.saveCardOptionEnabled)
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
        with(config.routeConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath(path)
                    .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                    .setCustomData(requestOptions.extraData)
                    .setCustomHeader(requestOptions.extraHeaders)
                    .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                    .build()
            )
        }
    }

    companion object {

        private const val ICON_ALPHA_ENABLED = 1f
        private const val ICON_ALPHA_DISABLED = 0.5f
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
    }
}