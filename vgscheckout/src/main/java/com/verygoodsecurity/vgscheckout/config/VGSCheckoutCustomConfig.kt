package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.util.extension.generateBaseUrl
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Holds configuration for vault payment processing with custom configuration.
 *
 * @param id unique organization vault id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
 */
@Parcelize
class VGSCheckoutCustomConfig internal constructor(
    override val id: String,
    override val routeId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutRouteConfig,
    override val formConfig: VGSCheckoutFormConfig,
    override val isScreenshotsAllowed: Boolean,
) : CheckoutConfig() {

    @IgnoredOnParcel
    override val baseUrl: String = generateBaseUrl()

    class Builder(
        private val vaultId: String
    ) {
        private var routeId: String = ""
        private var environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox()
        private var isScreenshotsAllowed = false

        private var expirationDateFieldName = ""
        private var expirationDateFieldSeparateSerializer: VGSDateSeparateSerializer? = null
        private var expirationDateFieldInputFormatRegex = DATE_FORMAT
        private var expirationDateFieldOutputFormatRegex = DATE_FORMAT

        private var cardNumberFieldName = ""
        private var isIconCardNumberHidden = false

        private var cvcFieldName = ""
        private var isIconCVCHidden = false

        private var cardHolderFieldName = ""
        private var cardHolderFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var countryFieldName = ""
        private var validCountries: List<String> = emptyList()
        private var countryFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var cityFieldName = ""
        private var cityFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var addressFieldName = ""
        private var addressFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var optionalAddressFieldName = ""
        private var optionalAddressFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var postalCodeFieldName = ""
        private var postalCodeFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var billingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN
        private var formValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT
        private var saveCardOptionEnabled = false

        private var path = ""
        private var hostnamePolicy: VGSCheckoutHostnamePolicy = VGSCheckoutHostnamePolicy.Vault
        private var httpMethod: VGSCheckoutHttpMethod = VGSCheckoutHttpMethod.POST
        private var extraHeaders: Map<String, String> = emptyMap()
        private var extraData: Map<String, @RawValue Any> = emptyMap()
        private var mergePolicy: VGSCheckoutDataMergePolicy =
            VGSCheckoutDataMergePolicy.FLAT_JSON

        /**
         * Defines type of vault.
         *
         * @param environment Type of vault.
         */
        fun setEnvironment(environment: VGSCheckoutEnvironment) = this.apply {
            this.environment = environment
        }

        /**
         * If true, checkout form will allow to make screenshots. Default is false.
         *
         * @param isScreenshotsAllowed Defines is screenshots allowed.
         */
        fun setIsScreenshotsAllowed(isScreenshotsAllowed: Boolean) = this.apply {
            this.isScreenshotsAllowed = isScreenshotsAllowed
        }

        /**
         * Defines route id for submitting data.
         *
         * @param routeId A route id.
         */
        fun setRouteId(routeId: String) = this.apply {
            this.routeId = routeId
        }

        // region Card options
        /**
         *  Defines if input field should be visible to user.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param isIconHidden defines if card brand icon should be hidden.
         */
        fun setCardNumberOptions(
            fieldName: String,
            isIconHidden: Boolean = false
        ) = this.apply {
            cardNumberFieldName = fieldName
            isIconCardNumberHidden = isIconHidden
        }

        /**
         * Card holder input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         */
        fun setCardHolderOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ) = this.apply {
            cardHolderFieldName = fieldName
            cardHolderFieldVisibility = visibility
        }

        /**
         * Card security code input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param isIconHidden defines if card brand icon should be hidden.
         */
        fun setCVCOptions(
            fieldName: String,
            isIconHidden: Boolean = false
        ) = this.apply {
            cvcFieldName = fieldName
            isIconCVCHidden = isIconHidden
        }

        /**
         * Expiration date input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param dateSeparateSerializer split date into separate JSON values before send to proxy.
         * @param inputFormatRegex ISO 8601 date input format.
         * @param outputFormatRegex ISO 8601 format in which date will be sent to proxy.
         */
        fun setExpirationDateOptions(
            fieldName: String,
            dateSeparateSerializer: VGSDateSeparateSerializer? = null,
            inputFormatRegex: String = DATE_FORMAT,
            outputFormatRegex: String = DATE_FORMAT
        ) = this.apply {
            expirationDateFieldName = fieldName
            expirationDateFieldSeparateSerializer = dateSeparateSerializer
            expirationDateFieldInputFormatRegex = inputFormatRegex
            expirationDateFieldOutputFormatRegex = outputFormatRegex
        }
        //endregion

        //region Billing address options
        /**
         * Country input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         * @param validCountries list of countries in ISO 3166-2 format that will be show in selection dialog.
         */
        fun setCountryOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE,
            validCountries: List<String> = emptyList()
        ) = this.apply {
            countryFieldName = fieldName
            countryFieldVisibility = visibility
            this.validCountries = validCountries
        }

        /**
         * City input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         */
        fun setCityOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ) = this.apply {
            cityFieldName = fieldName
            cityFieldVisibility = visibility
        }

        /**
         * Address input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         */
        fun setAddressOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ) = this.apply {
            addressFieldName = fieldName
            addressFieldVisibility = visibility
        }

        /**
         * Optional address input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         */
        fun setOptionalAddressOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ) = this.apply {
            optionalAddressFieldName = fieldName
            optionalAddressFieldVisibility = visibility
        }

        /**
         * Postal code input field options.
         *
         * @param fieldName text to be used for data transfer to VGS proxy.
         * @param visibility defines if input field should be visible to user.
         */
        fun setPostalCodeOptions(
            fieldName: String,
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ) = this.apply {
            postalCodeFieldName = fieldName
            postalCodeFieldVisibility = visibility
        }

        /**
         * Defines if address section UI should be visible to user.
         *
         * @param visibility Address section visibility.
         */
        fun setBillingAddressVisibility(
            visibility: VGSCheckoutBillingAddressVisibility
        ) = this.apply {
            billingAddressVisibility = visibility
        }
        //endregion

        /**
         * Defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
         *
         * @param validationBehaviour Validation behavior.
         */
        fun setFormValidationBehaviour(
            validationBehaviour: VGSCheckoutFormValidationBehaviour
        ) = this.apply {
            formValidationBehaviour = validationBehaviour
        }

        //region Route config
        /**
         * Defines inbound rout path for your organization vault.
         *
         * @param path Inbound rout path.
         */
        fun setPath(path: String) = this.apply {
            this.path = path
        }

        /**
         * Defines type of base url to send data.
         *
         * @param hostnamePolicy type of base url to send data.
         */
        fun setHostnamePolicy(hostnamePolicy: VGSCheckoutHostnamePolicy) = this.apply {
            this.hostnamePolicy = hostnamePolicy
        }

        /**
         * Define http method.
         *
         * @param method Http method
         */
        fun setHttpMethod(method: VGSCheckoutHttpMethod) = this.apply {
            httpMethod = method
        }

        /**
         * Defines request headers.
         *
         * @param headers Request headers.
         */
        fun setHeaders(headers: Map<String, String>) = this.apply {
            extraHeaders = headers
        }

        /**
         * Defines extra request payload data.
         *
         * @param data An extra request payload data.
         */
        fun setPayload(data: Map<String, Any>) = this.apply {
            extraData = data
        }

        /**
         * Define field name mapping policy and how fields data and extra data should be.
         *
         * @param policy A field name mapping policy.
         */
        fun setMergePolicy(policy: VGSCheckoutDataMergePolicy) = this.apply {
            mergePolicy = policy
        }
        //endregion

        /**
         * Creates custom configuration.
         */
        fun build(): VGSCheckoutCustomConfig {
            val formConfig = buildFormConfig()
            val routeConfig = buildRouteConfig()

            return VGSCheckoutCustomConfig(
                vaultId,
                routeId,
                environment,
                routeConfig,
                formConfig,
                isScreenshotsAllowed
            )
        }

        private fun buildFormConfig(): VGSCheckoutFormConfig {
            val cardOptions = buildCardOptions()
            val billingAddressOptions = buildBillingAddressOptions()

            return VGSCheckoutFormConfig(
                cardOptions,
                billingAddressOptions,
                formValidationBehaviour,
                saveCardOptionEnabled
            )
        }

        private fun buildCardOptions(): VGSCheckoutCardOptions {
            return VGSCheckoutCardOptions(
                VGSCheckoutCardNumberOptions(
                    cardNumberFieldName,
                    isIconCardNumberHidden
                ),
                VGSCheckoutCardHolderOptions(
                    cardHolderFieldName,
                    cardHolderFieldVisibility
                ),
                VGSCheckoutCVCOptions(
                    cvcFieldName,
                    isIconCVCHidden
                ),
                VGSCheckoutExpirationDateOptions(
                    expirationDateFieldName,
                    expirationDateFieldSeparateSerializer,
                    expirationDateFieldInputFormatRegex,
                    expirationDateFieldOutputFormatRegex
                )
            )
        }

        private fun buildBillingAddressOptions(): VGSCheckoutBillingAddressOptions {
            return VGSCheckoutBillingAddressOptions(
                VGSCheckoutCountryOptions(
                    countryFieldName,
                    validCountries,
                    countryFieldVisibility
                ),
                VGSCheckoutCityOptions(
                    cityFieldName,
                    cityFieldVisibility
                ),
                VGSCheckoutAddressOptions(
                    addressFieldName,
                    addressFieldVisibility
                ),
                VGSCheckoutOptionalAddressOptions(
                    optionalAddressFieldName,
                    optionalAddressFieldVisibility
                ),
                VGSCheckoutPostalCodeOptions(
                    postalCodeFieldName,
                    postalCodeFieldVisibility
                ),
                billingAddressVisibility
            )
        }

        private fun buildRouteConfig(): VGSCheckoutRouteConfig {
            val requestOptions = VGSCheckoutRequestOptions(
                httpMethod,
                extraHeaders,
                extraData,
                mergePolicy
            )

            return VGSCheckoutRouteConfig(
                path,
                hostnamePolicy,
                requestOptions
            )
        }

        companion object {

            private const val DATE_FORMAT = "MM/yy"
        }
    }
}
