package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Size
import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.config.core.OrchestrationConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.OrderDetails
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod.SavedCards.Companion.MAX_CARDS_SIZE
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutPaymentFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutPaymentCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPaymentPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.GetOrderDetails
import com.verygoodsecurity.vgscheckout.networking.command.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.CompositeCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable

internal class VGSCheckoutPaymentConfig internal constructor(
    override val accessToken: String,
    override val routeId: String,
    override val id: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutPaymentFormConfig,
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

    internal var orderDetails: OrderDetails? = null
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) internal set

    override val baseUrl: String = generateBaseUrl()

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutPaymentRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutPaymentFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        true,
    ) {
        this.savedCards = mutableListOf<Card>().apply {
            parcel.readList(
                this,
                Card::class.java.classLoader
            )
        }
        this.orderDetails = parcel.readParcelable(OrderDetails::class.java.classLoader)
    }

    override fun describeContents(): Int = 0

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
        parcel.writeParcelable(orderDetails, flags)
    }

    class Builder(
        private val tenantId: String
    ) {
        private var environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox()
        private var isScreenshotsAllowed = false
        private var accessToken = ""
        private var orderId = ""
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
         * Defines route id for submitting data.
         *
         * @param routeId A route id.
         */
        fun setRouteId(routeId: String): Builder {
            this.routeId = routeId
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
         * Defines payment order Id.
         *
         * @param orderId Id of an order.
         */
        fun setOrderId(orderId: String): Builder {
            this.orderId = orderId
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
            visibility: VGSCheckoutFieldVisibility
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
            visibility: VGSCheckoutFieldVisibility
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

        private fun buildFormConfig(): VGSCheckoutPaymentFormConfig {
            return VGSCheckoutPaymentFormConfig(
                createOrchestrationCardOptions(),
                VGSCheckoutPaymentBillingAddressOptions(
                    VGSCheckoutPaymentCountryOptions(
                        validCountries,
                        countryFieldVisibility
                    ),
                    VGSCheckoutPaymentCityOptions(cityFieldVisibility),
                    VGSCheckoutPaymentAddressOptions(addressFieldVisibility),
                    VGSCheckoutPaymentOptionalAddressOptions(optionalAddressFieldVisibility),
                    VGSCheckoutPaymentPostalCodeOptions(postalCodeFieldVisibility),
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
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig>? = null
        ): VGSCheckoutCancellable {
            val formConfig = buildFormConfig()

            return create(
                context,
                accessToken,
                orderId,
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

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal fun build(): VGSCheckoutPaymentConfig {
            val formConfig = buildFormConfig()
            return VGSCheckoutPaymentConfig(
                accessToken,
                routeId,
                tenantId,
                environment,
                VGSCheckoutPaymentRouteConfig(accessToken),
                formConfig,
                isScreenshotsAllowed,
                isRemoveCardOptionEnabled,
                false
            )
        }
    }

    companion object {

        @Suppress("unused")
        @JvmField
        internal val CREATOR = object : Parcelable.Creator<VGSCheckoutPaymentConfig> {

            override fun createFromParcel(parcel: Parcel): VGSCheckoutPaymentConfig {
                return VGSCheckoutPaymentConfig(parcel)
            }

            override fun newArray(size: Int): Array<VGSCheckoutPaymentConfig?> {
                return arrayOfNulls(size)
            }
        }

        private fun create(
            context: Context,
            accessToken: String,
            orderId: String,
            routeId: String,
            tenantId: String,
            paymentMethod: VGSCheckoutPaymentMethod.SavedCards,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutPaymentFormConfig,
            isScreenshotsAllowed: Boolean = false,
            isRemoveCardOptionEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig>? = null
        ): VGSCheckoutCancellable {
            val config = VGSCheckoutPaymentConfig(
                accessToken,
                routeId,
                tenantId,
                environment,
                VGSCheckoutPaymentRouteConfig(accessToken),
                formConfig,
                isScreenshotsAllowed,
                isRemoveCardOptionEnabled,
                false
            )
            val ids = paymentMethod.getIds()

            val saveCardCommand = with(config) {
                GetSavedCardsCommand(
                    context,
                    GetSavedCardsCommand.Params(
                        baseUrl,
                        routeConfig.path,
                        accessToken,
                        ids
                    )
                )
            }

            val orderCommand = with(config) {
                GetOrderDetails(
                    context,
                    GetOrderDetails.Params(
                        baseUrl,
                        orderId,
                        accessToken
                    )
                )
            }

            return CompositeCommand(
                context,
                mutableListOf(orderCommand, saveCardCommand)
            ).also {
                it.execute {
                    when (val result = it.intermediateResult) {
                        is GetSavedCardsCommand.Result -> saveCardsDetails(config, result)
                        is GetOrderDetails.Result -> saveOrderDetails(config, result)
                    }

                    if (it.isProcessing.not()) callback?.onSuccess(config)
                    //todo Think if we need this callback because here we have orders, payment initialization in future too.
//                    callback?.onFailure(result.exception)
                }
            }
        }

        private fun saveOrderDetails(
            config: VGSCheckoutPaymentConfig,
            result: GetOrderDetails.Result
        ) {
            config.orderDetails = result.orderDetails
        }

        private fun saveCardsDetails(
            config: VGSCheckoutPaymentConfig,
            result: GetSavedCardsCommand.Result
        ) {
            when (result) {
                is GetSavedCardsCommand.Result.Success -> {
                    //todo: log FinInstrumentCrudEvent.load for transfers. Success
                    config.savedCards = result.cards
                }
                is GetSavedCardsCommand.Result.Failure -> {
                    //todo: log FinInstrumentCrudEvent.load for transfers. Failure
                }
            }
        }
    }
}