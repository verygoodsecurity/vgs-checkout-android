package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
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
class VGSCheckoutCustomConfig @JvmOverloads constructor(
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
            validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT
        ): Builder {
            formValidationBehaviour = validationBehaviour
            return this
        }

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

        fun build(): VGSCheckoutCustomConfig {
            val formConfig = buildFormConfig()

            return VGSCheckoutCustomConfig(
                vaultId = vaultId,
                routeConfig = VGSCheckoutCustomRouteConfig(),
                formConfig = formConfig
            )
        }

        companion object {

            private const val DATE_FORMAT = "MM/yy"
        }
    }
}
