package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.config.core.OrchestrationConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl

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
    override val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutAddCardFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isRemoveCardOptionEnabled: Boolean,
    createdFromParcel: Boolean
) : OrchestrationConfig(
    accessToken,
    tenantId,
    environment,
    routeConfig,
    formConfig,
    isScreenshotsAllowed,
    isRemoveCardOptionEnabled,
    createdFromParcel
) {

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutPaymentRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutAddCardFormConfig::class.java.classLoader)!!,
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
    constructor(
        accessToken: String,
        tenantId: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        formConfig: VGSCheckoutAddCardFormConfig = VGSCheckoutAddCardFormConfig(),
        isScreenshotsAllowed: Boolean = false
    ) : this(
        accessToken,
        tenantId,
        environment,
        VGSCheckoutPaymentRouteConfig(accessToken),
        formConfig,
        isScreenshotsAllowed,
        true,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(tenantId)
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

        /**
         * Function that allows create config with saved cards.
         *
         * @param accessToken payment orchestration app access token.
         * @param tenantId unique organization id.
         * @param paymentMethod
         * @param environment type of vault.
         * @param formConfig UI configuration.
         * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
         * @param isRemoveCardOptionEnabled If true, user will be able to delete saved card.
         */
        @JvmOverloads
        fun create(
            context: Context,
            accessToken: String,
            tenantId: String,
            paymentMethod: VGSCheckoutPaymentMethod.SavedCards,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutAddCardFormConfig = VGSCheckoutAddCardFormConfig(),
            isScreenshotsAllowed: Boolean = false,
            isRemoveCardOptionEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutAddCardConfig>? = null
        ): VGSCheckoutCancellable {
            val config = VGSCheckoutAddCardConfig(
                accessToken,
                tenantId,
                environment,
                VGSCheckoutPaymentRouteConfig(accessToken),
                formConfig,
                isScreenshotsAllowed,
                isRemoveCardOptionEnabled,
                false
            )
            val ids = paymentMethod.getIds()
            val params = GetSavedCardsCommand.Params(
                config.getBaseUrl(context),
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