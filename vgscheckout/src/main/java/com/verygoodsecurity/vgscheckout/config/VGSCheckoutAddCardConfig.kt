package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Size
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.config.core.OrchestrationConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod.SavedCards.Companion.MAX_CARDS_SIZE
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable

/**
 * Holds configuration with predefined setup for work with payment orchestration app.
 *
 * @param accessToken payment orchestration app access token.
 * @param tenantId unique organization id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
 * @param createdFromParcel if true then object created form parcel. Used to determine if access token
 * validation event should be send.
 * @property savedCards previously saved card(financial instruments).
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutAddCardConfig internal constructor(
    override val accessToken: String,
    override val routeId: String,
    override val id: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutRouteConfig,
    override val formConfig: VGSCheckoutFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isRemoveCardOptionEnabled: Boolean,
    createdFromParcel: Boolean
) : OrchestrationConfig(
    accessToken,
    routeId,
    id,
    environment,
    routeConfig,
    formConfig,
    isScreenshotsAllowed,
    isRemoveCardOptionEnabled,
    createdFromParcel
) {

    override val baseUrl: String = generateBaseUrl()

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        true
    ) {
        this.savedCards = mutableListOf<Card>().apply {
            parcel.readList(
                this,
                Card::class.java.classLoader
            )
        }
    }

    /**
     * Public constructor.
     *
     * @param accessToken payment orchestration app access token.
     * @param tenantId unique organization id.
     * @param environment type of vault.
     * @param formConfig UI configuration.
     * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
     *
     * @throws com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
     */
    @JvmOverloads
    @Throws(VGSCheckoutException::class)
    internal constructor(
        accessToken: String,
        routeId: String,
        tenantId: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        formConfig: VGSCheckoutFormConfig,
        isScreenshotsAllowed: Boolean = false
    ) : this(
        accessToken,
        routeId,
        tenantId,
        environment,
        createRouteConfig(accessToken),
        formConfig,
        isScreenshotsAllowed,
        true,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(routeId)
        parcel.writeString(id)
        parcel.writeParcelable(environment, flags)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
        parcel.writeInt(if (isScreenshotsAllowed) 1 else 0)
        parcel.writeInt(if (isRemoveCardOptionEnabled) 1 else 0)
        parcel.writeList(savedCards)
    }

    override fun describeContents(): Int {
        return 0
    }

    class Builder(
        private val tenantId: String
    ) {

        private var environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox()
        private var isScreenshotsAllowed = false
        private var accessToken = ""
        private var routeId = PAYMENT_URL_ROUTE_ID
        private var cardIds: List<String> = arrayListOf()
        private var isRemoveCardOptionEnabled: Boolean = true

        private var countryFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        private var validCountries: List<String> = emptyList()

        private var cityFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var addressFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var optionalAddressFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var postalCodeFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

        private var billingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN
        private var formValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT
        private var saveCardOptionEnabled = false

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

        /**
         * Defines payment orchestration app access token.
         *
         * @param accessToken A payment orchestration app access token.
         */
        fun setAccessToken(accessToken: String): Builder {
            this.accessToken = accessToken
            return this
        }

        /**
         * Defines route id for submitting data.
         *
         * @param routeId A route id.
         */
        fun setRouteId(routeId: String): Builder {
            this.routeId = routeId
            return this
        }

        /**
         * Add ability to use previously saved cards or new card.
         *
         * @param cardIds list of cards(financial instruments) ids. Max length [MAX_CARDS_SIZE].
         */
        fun setSavedCardIds(
            @Size(max = MAX_CARDS_SIZE) cardIds: List<String>
        ): Builder {
            this.cardIds = cardIds
            return this
        }

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

        /**
         * Defines if save card checkbox should be visible.
         *
         * @param isSaveCardOptionVisible save card checkbox visibility.
         */
        fun setIsSaveCardOptionVisible(
            isSaveCardOptionVisible: Boolean
        ): Builder {
            saveCardOptionEnabled = isSaveCardOptionVisible
            return this
        }

        /**
         * Defines if saved cards could be removed by user.
         *
         * @param isRemoveCardOptionEnabled Possibility to remove cards.
         */
        fun setIsRemoveCardOptionEnabled(
            isRemoveCardOptionEnabled: Boolean
        ): Builder {
            this.isRemoveCardOptionEnabled = isRemoveCardOptionEnabled
            return this
        }

        //region Form config
        /**
         * Country input field options.
         *
         * @param visibility defines if input field should be visible to user.
         * @param validCountries list of countries in ISO 3166-2 format that will be show in selection dialog.
         */
        fun setCountryOptions(
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE,
            validCountries: List<String> = emptyList()
        ): Builder {
            countryFieldVisibility = visibility
            this.validCountries = validCountries
            return this
        }

        /**
         * City input field options.
         *
         * @param visibility defines if input field should be visible to user.
         */
        fun setCityOptions(
            visibility: VGSCheckoutFieldVisibility
        ): Builder {
            cityFieldVisibility = visibility
            return this
        }

        /**
         * Address input field options.
         *
         * @param visibility defines if input field should be visible to user.
         */
        fun setAddressOptions(
            visibility: VGSCheckoutFieldVisibility
        ): Builder {
            addressFieldVisibility = visibility
            return this
        }

        /**
         * Optional address input field options.
         *
         * @param visibility defines if input field should be visible to user.
         */
        fun setOptionalAddressOptions(
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ): Builder {
            optionalAddressFieldVisibility = visibility
            return this
        }

        /**
         * Postal code input field options.
         *
         * @param visibility defines if input field should be visible to user.
         */
        fun setPostalCodeOptions(
            visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
        ): Builder {
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

        private fun buildFormConfig(): VGSCheckoutFormConfig {
            return VGSCheckoutFormConfig(
                createCardOptions(),
                VGSCheckoutBillingAddressOptions(
                    VGSCheckoutCountryOptions(
                        COUNTRY_FIELD_NAME,
                        validCountries,
                        countryFieldVisibility
                    ),
                    VGSCheckoutCityOptions(
                        CITY_FIELD_NAME,
                        cityFieldVisibility
                    ),
                    VGSCheckoutAddressOptions(
                        ADDRESS_FIELD_NAME,
                        addressFieldVisibility
                    ),
                    VGSCheckoutOptionalAddressOptions(
                        OPTIONAL_FIELD_NAME,
                        optionalAddressFieldVisibility
                    ),
                    VGSCheckoutPostalCodeOptions(
                        POSTAL_CODE_FIELD_NAME,
                        postalCodeFieldVisibility
                    ),
                    billingAddressVisibility
                ),
                formValidationBehaviour,
                saveCardOptionEnabled
            )
        }
        //endregion

        /**
         * Creates VGSCheckoutAddCardConfig configuration.
         */
        fun build(
            context: Context,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutAddCardConfig>? = null
        ): VGSCheckoutCancellable {
            val formConfig = buildFormConfig()

            return create(
                context,
                accessToken,
                routeId,
                tenantId,
                VGSCheckoutPaymentMethod.SavedCards(cardIds),
                environment,
                formConfig,
                isScreenshotsAllowed,
                isRemoveCardOptionEnabled,
                callback
            )
        }

        fun build(): VGSCheckoutAddCardConfig {
            val formConfig = buildFormConfig()
            return VGSCheckoutAddCardConfig(
                accessToken,
                routeId,
                tenantId,
                environment,
                formConfig,
                isScreenshotsAllowed
            )
        }
    }

    companion object {

        @Suppress("unused")
        @JvmField
        internal val CREATOR = object : Parcelable.Creator<VGSCheckoutAddCardConfig> {

            override fun createFromParcel(parcel: Parcel): VGSCheckoutAddCardConfig {
                return VGSCheckoutAddCardConfig(parcel)
            }

            override fun newArray(size: Int): Array<VGSCheckoutAddCardConfig?> {
                return arrayOfNulls(size)
            }
        }

        private fun create(
            context: Context,
            accessToken: String,
            routeId: String,
            tenantId: String,
            paymentMethod: VGSCheckoutPaymentMethod.SavedCards,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutFormConfig,
            isScreenshotsAllowed: Boolean = false,
            isRemoveCardOptionEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutAddCardConfig>? = null
        ): VGSCheckoutCancellable {
            val config = VGSCheckoutAddCardConfig(
                accessToken,
                routeId,
                tenantId,
                environment,
                createRouteConfig(accessToken),
                formConfig,
                isScreenshotsAllowed,
                isRemoveCardOptionEnabled,
                false
            )
            val ids = paymentMethod.getIds()
            val params = GetSavedCardsCommand.Params(
                config.baseUrl,
                config.routeConfig.path,
                accessToken,
                ids
            )
            val command = GetSavedCardsCommand(context, params)
            command.execute {
                when (it) {
                    is GetSavedCardsCommand.Result.Success -> {
                        config.analyticTracker.log(
                            FinInstrumentCrudEvent.load(
                                FinInstrumentCrudEvent.DEFAULT_CODE,
                                true,
                                null,
                                false,
                                ids.count(),
                                ids.count() - it.cards.count()
                            )
                        )
                        config.savedCards = it.cards
                        callback?.onSuccess(config)
                    }
                    is GetSavedCardsCommand.Result.Failure -> {
                        config.analyticTracker.log(
                            FinInstrumentCrudEvent.load(
                                it.exception.code,
                                false,
                                it.exception.message,
                                false,
                                ids.count(),
                                ids.count(),
                            )
                        )
                        callback?.onFailure(it.exception)
                    }
                }
            }
            return command
        }
    }
}