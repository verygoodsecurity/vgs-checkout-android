package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCustomCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutCustomPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCustomCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCustomCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCustomCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutCustomExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Holds configuration for vault payment processing with custom configuration.
 *
 * @param vaultId unique organization vault id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
 */
@Parcelize
class VGSCheckoutCustomConfig internal constructor(
    val vaultId: String,
    override val environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
    override val routeConfig: VGSCheckoutCustomRouteConfig = VGSCheckoutCustomRouteConfig(),
    override val formConfig: VGSCheckoutCustomFormConfig = VGSCheckoutCustomFormConfig(),
    override val isScreenshotsAllowed: Boolean = false,
) : CheckoutConfig(vaultId) {

    @IgnoredOnParcel
    override val baseUrl: String = generateBaseUrl(false)

    class Builder(
        private val vaultId: String
    ) {
        private var environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox()
        private var isScreenshotsAllowed = false

        private var expirationDateFieldSeparateSerializer: VGSDateSeparateSerializer? = null
        private var expirationDateFieldName = ""
        private var expirationDateFieldInputFormatRegex = DATE_FORMAT
        private var expirationDateFieldOutputFormatRegex = DATE_FORMAT

        private var cardNumberFieldName = ""
        private var isIconCardNumberHidden = false

        private var cvcFieldName = ""
        private var isIconCVCHidden = false

        private var cardHolderFieldName = ""
        private var cardHolderFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var countryFieldName = ""
        private var countryFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        private var validCountries: List<String> = emptyList()

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
            VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE

        /**
         * Defines type of vault.
         *
         * @param environment Type of vault.
         */
        fun setEnvironment(environment: VGSCheckoutEnvironment): Builder {
            this.environment = environment
            return this
        }

        /**
         * If true, checkout form will allow to make screenshots. Default is false.
         *
         * @param isScreenshotsAllowed Defines is screenshots allowed.
         */
        fun setIsScreenshotsAllowed(isScreenshotsAllowed: Boolean): Builder {
            this.isScreenshotsAllowed = isScreenshotsAllowed
            return this
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
        ): Builder {
            cardNumberFieldName = fieldName
            isIconCardNumberHidden = isIconHidden
            return this
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
        ): Builder {
            cardHolderFieldName = fieldName
            cardHolderFieldVisibility = visibility
            return this
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
        ): Builder {
            cvcFieldName = fieldName
            isIconCVCHidden = isIconHidden
            return this
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
        ): Builder {
            expirationDateFieldName = fieldName
            expirationDateFieldSeparateSerializer = dateSeparateSerializer
            expirationDateFieldInputFormatRegex = inputFormatRegex
            expirationDateFieldOutputFormatRegex = outputFormatRegex
            return this
        }

        private fun buildCardOptions(): VGSCheckoutCustomCardOptions {
            return VGSCheckoutCustomCardOptions(
                VGSCheckoutCustomCardNumberOptions(
                    cardNumberFieldName,
                    isIconCardNumberHidden
                ),
                VGSCheckoutCustomCardHolderOptions(
                    cardHolderFieldName,
                    cardHolderFieldVisibility
                ),
                VGSCheckoutCustomCVCOptions(
                    cvcFieldName,
                    isIconCVCHidden
                ),
                VGSCheckoutCustomExpirationDateOptions(
                    expirationDateFieldName,
                    expirationDateFieldSeparateSerializer,
                    expirationDateFieldInputFormatRegex,
                    expirationDateFieldOutputFormatRegex
                )
            )
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
        ): Builder {
            countryFieldName = fieldName
            countryFieldVisibility = visibility
            this.validCountries = validCountries
            return this
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
        ): Builder {
            cityFieldName = fieldName
            cityFieldVisibility = visibility
            return this
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
        ): Builder {
            addressFieldName = fieldName
            addressFieldVisibility = visibility
            return this
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
        ): Builder {
            optionalAddressFieldName = fieldName
            optionalAddressFieldVisibility = visibility
            return this
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
        ): Builder {
            postalCodeFieldName = fieldName
            postalCodeFieldVisibility = visibility
            return this
        }

        /**
         * Defines if address section UI should be visible to user.
         *
         * @param visibility Address section visibility.
         */
        fun setBillingAddressVisibility(
            visibility: VGSCheckoutBillingAddressVisibility
        ): Builder {
            billingAddressVisibility = visibility
            return this
        }


        private fun buildBillingAddressOptions(): VGSCheckoutCustomBillingAddressOptions {
            return VGSCheckoutCustomBillingAddressOptions(
                VGSCheckoutCustomCountryOptions(
                    countryFieldName,
                    validCountries,
                    countryFieldVisibility
                ),
                VGSCheckoutCustomCityOptions(
                    cityFieldName,
                    cityFieldVisibility
                ),
                VGSCheckoutCustomAddressOptions(
                    addressFieldName,
                    addressFieldVisibility
                ),
                VGSCheckoutCustomOptionalAddressOptions(
                    optionalAddressFieldName,
                    optionalAddressFieldVisibility
                ),
                VGSCheckoutCustomPostalCodeOptions(
                    postalCodeFieldName,
                    postalCodeFieldVisibility
                ),
                billingAddressVisibility
            )
        }
        //endregion

        /**
         * Defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
         *
         * @param validationBehaviour Validation behavior.
         */
        fun setFormValidationBehaviour(
            validationBehaviour: VGSCheckoutFormValidationBehaviour
        ): Builder {
            formValidationBehaviour = validationBehaviour
            return this
        }

        //region Route config
        /**
         * Defines inbound rout path for your organization vault.
         *
         * @param path Inbound rout path.
         */
        fun setPath(path: String): Builder {
            this.path = path
            return this
        }

        /**
         * Defines type of base url to send data.
         *
         * @param hostnamePolicy type of base url to send data.
         */
        fun setHostnamePolicy(hostnamePolicy: VGSCheckoutHostnamePolicy): Builder {
            this.hostnamePolicy = hostnamePolicy
            return this
        }

        /**
         * Define http method.
         *
         * @param method Http method
         */
        fun setHttpMethod(method: VGSCheckoutHttpMethod): Builder {
            httpMethod = method
            return this
        }

        /**
         * Defines request headers.
         *
         * @param headers Request headers.
         */
        fun setHeaders(headers: Map<String, String>): Builder {
            extraHeaders = headers
            return this
        }

        /**
         * Defines extra request payload data.
         *
         * @param data An extra request payload data.
         */
        fun setRequestPayload(data: Map<String, Any>): Builder {
            extraData = data
            return this
        }

        /**
         * Define field name mapping policy and how fields data and extra data should be.
         *
         * @param policy A field name mapping policy.
         */
        fun setMergePolicy(policy: VGSCheckoutDataMergePolicy): Builder {
            mergePolicy = policy
            return this
        }

        private fun buildRouteConfig(): VGSCheckoutCustomRouteConfig {
            val requestOptions = VGSCheckoutCustomRequestOptions(
                httpMethod,
                extraHeaders,
                extraData,
                mergePolicy
            )

            return VGSCheckoutCustomRouteConfig(
                path,
                hostnamePolicy,
                requestOptions
            )
        }
        //endregion

        private fun buildFormConfig(): VGSCheckoutCustomFormConfig {
            val cardOptions = buildCardOptions()
            val billingAddressOptions = buildBillingAddressOptions()

            return VGSCheckoutCustomFormConfig(
                cardOptions,
                billingAddressOptions,
                formValidationBehaviour,
                saveCardOptionEnabled
            )
        }

        /**
         * Creates custom configuration.
         */
        fun build(): VGSCheckoutCustomConfig {
            val formConfig = buildFormConfig()
            val routeConfig = buildRouteConfig()

            return VGSCheckoutCustomConfig(
                vaultId,
                environment,
                routeConfig,
                formConfig,
                isScreenshotsAllowed
            )
        }

        companion object {

            private const val DATE_FORMAT = "MM/yy"
        }
    }
}
