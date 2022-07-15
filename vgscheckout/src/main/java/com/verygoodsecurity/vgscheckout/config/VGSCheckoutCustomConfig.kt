package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
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
    override val environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
    override val routeConfig: VGSCheckoutCustomRouteConfig = VGSCheckoutCustomRouteConfig(),
    override val formConfig: VGSCheckoutFormConfig = VGSCheckoutFormConfig(),
    override val isScreenshotsAllowed: Boolean = false,
) : CheckoutConfig() {

    @IgnoredOnParcel
    override val baseUrl: String = generateBaseUrl()

    class Builder(
        private val vaultId: String
    ) {
        private var routeId: String = ""
        private var environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox()
        private var isScreenshotsAllowed = false

        private var cardNumberOptions: VGSCheckoutCardNumberOptions = VGSCheckoutCardNumberOptions()
        private var cardHolderOptions: VGSCheckoutCardHolderOptions = VGSCheckoutCardHolderOptions()
        private var cvcOptions: VGSCheckoutCVCOptions = VGSCheckoutCVCOptions()
        private var expirationDateOptions: VGSCheckoutExpirationDateOptions =
            VGSCheckoutExpirationDateOptions()

        private var countryOptions: VGSCheckoutCountryOptions = VGSCheckoutCountryOptions()
        private var cityOptions: VGSCheckoutCityOptions = VGSCheckoutCityOptions()
        private var addressOptions: VGSCheckoutAddressOptions = VGSCheckoutAddressOptions()
        private var optionalAddressOptions: VGSCheckoutOptionalAddressOptions =
            VGSCheckoutOptionalAddressOptions()
        private var postalCodeOptions: VGSCheckoutPostalCodeOptions = VGSCheckoutPostalCodeOptions()
        private var billingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN

        private var formValidationBehaviour: VGSCheckoutFormValidationBehaviour =
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT
        private var saveCardOptionEnabled: Boolean = false

        private var path: String = ""
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
            cardNumberOptions = VGSCheckoutCardNumberOptions(fieldName, isIconHidden)
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
            cardHolderOptions = VGSCheckoutCardHolderOptions(fieldName, visibility)
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
            cvcOptions = VGSCheckoutCVCOptions(fieldName, isIconHidden)
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
            inputFormatRegex: String = VGSCheckoutExpirationDateOptions.DATE_FORMAT,
            outputFormatRegex: String = VGSCheckoutExpirationDateOptions.DATE_FORMAT
        ) = this.apply {
            expirationDateOptions = VGSCheckoutExpirationDateOptions(
                fieldName,
                dateSeparateSerializer,
                inputFormatRegex,
                outputFormatRegex
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
        ) = this.apply {
            countryOptions = VGSCheckoutCountryOptions(fieldName, validCountries, visibility)
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
            cityOptions = VGSCheckoutCityOptions(fieldName, visibility)
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
            addressOptions = VGSCheckoutAddressOptions(fieldName, visibility)
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
            optionalAddressOptions = VGSCheckoutOptionalAddressOptions(fieldName, visibility)
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
            postalCodeOptions = VGSCheckoutPostalCodeOptions(fieldName, visibility)
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

        private fun buildCardOptions(): VGSCheckoutCardOptions = VGSCheckoutCardOptions(
            cardNumberOptions,
            cardHolderOptions,
            cvcOptions,
            expirationDateOptions
        )

        private fun buildBillingAddressOptions(): VGSCheckoutBillingAddressOptions =
            VGSCheckoutBillingAddressOptions(
                countryOptions,
                cityOptions,
                addressOptions,
                optionalAddressOptions,
                postalCodeOptions,
                billingAddressVisibility
            )

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
    }
}
