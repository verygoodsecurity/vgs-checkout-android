package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.analytic.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.networking.command.saved.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.setupURL
import java.util.*

/**
 * Holds configuration with predefined setup for work with payment orchestration app.
 *
 * @param accessToken payment orchestration app access token.
 * @param tenantId unique organization vault id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
 * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues if any occurs.
 * @param savedCards previously saved card(financial instruments).
 * @param createdFromParcel if true then object created form parcel. Used to determine if access token
 * validation event should be send.
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutAddCardConfig private constructor(
    internal val accessToken: String,
    val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutAddCardFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isAnalyticsEnabled: Boolean,
    internal val savedCards: List<Card>,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

    init {
        //TODO: Uncomment token validation
//        if (!createdFromParcel) validateToken()
    }

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutPaymentRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutAddCardFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        LinkedList<Card>().apply {
            parcel.readList(
                this,
                Card::class.java.classLoader
            )
        },
        true
    )

    /**
     * Public constructor.
     *
     * @param accessToken payment orchestration app access token.
     * @param tenantId unique organization id.
     * @param environment type of vault.
     * @param formConfig UI configuration.
     * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
     * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug
     * issues if any occurs. Default value is true.
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
        isScreenshotsAllowed: Boolean = false,
        isAnalyticsEnabled: Boolean = true
    ) : this(
        accessToken,
        tenantId,
        environment,
        VGSCheckoutPaymentRouteConfig(accessToken),
        formConfig,
        isScreenshotsAllowed,
        isAnalyticsEnabled,
        emptyList(),
        false
    )

    @JvmOverloads
    @Throws(VGSCheckoutException::class)
    internal constructor(
        accessToken: String,
        tenantId: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        formConfig: VGSCheckoutAddCardFormConfig = VGSCheckoutAddCardFormConfig(),
        isScreenshotsAllowed: Boolean = false,
        isAnalyticsEnabled: Boolean = true,
        savedCards: List<Card>
    ) : this(
        accessToken,
        tenantId,
        environment,
        VGSCheckoutPaymentRouteConfig(accessToken),
        formConfig,
        isScreenshotsAllowed,
        isAnalyticsEnabled,
        savedCards,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(tenantId)
        parcel.writeParcelable(environment, flags)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
        parcel.writeInt(if (isScreenshotsAllowed) 1 else 0)
        parcel.writeInt(if (isAnalyticsEnabled) 1 else 0)
        parcel.writeList(savedCards)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    private fun validateToken() {
        try {
            CheckoutCredentialsValidator.validateJWT(accessToken)
            analyticTracker.log(JWTValidationEvent(true))
        } finally {
            analyticTracker.log(JWTValidationEvent(false))
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

        @JvmOverloads
        fun create(
            context: Context,
            accessToken: String,
            tenantId: String,
            paymentMethod: VGSCheckoutPaymentMethod.SavedCards,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutAddCardFormConfig = VGSCheckoutAddCardFormConfig(),
            isScreenshotsAllowed: Boolean = false,
            isAnalyticsEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutAddCardConfig>? = null
        ): VGSCheckoutCancellable {
            val params = GetSavedCardsCommand.Params(
                tenantId.setupURL(environment.value),
                VGSCheckoutPaymentRouteConfig.PATH,
                accessToken,
                paymentMethod.getIds()
            )
            val command = GetSavedCardsCommand(context)
            command.execute(params) {
                when (it) {
                    is GetSavedCardsCommand.Result.Success -> {
                        try {
                            callback?.onSuccess(
                                VGSCheckoutAddCardConfig(
                                    accessToken,
                                    tenantId,
                                    environment,
                                    formConfig,
                                    isScreenshotsAllowed,
                                    isAnalyticsEnabled,
                                    it.cards
                                )
                            )
                        } catch (e: VGSCheckoutException) {
                            callback?.onFailure(e)
                        }
                    }
                    is GetSavedCardsCommand.Result.Failure -> callback?.onFailure(it.exception)
                }
            }
            return command
        }
    }
}