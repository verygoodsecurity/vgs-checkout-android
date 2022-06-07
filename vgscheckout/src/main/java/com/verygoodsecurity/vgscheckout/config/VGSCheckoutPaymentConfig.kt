package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutPaymentFormConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl

class VGSCheckoutPaymentConfig internal constructor(
    internal val accessToken: String,
    val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutPaymentFormConfig,
    override val isScreenshotsAllowed: Boolean,
    val isRemoveCardOptionEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

    internal var savedCards: List<Card> = emptyList()
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) internal set

    //region duplicated from AddCardConfig

    init {
        //TODO: Uncomment token validation
        //TODO: Uncomment tests in VGSCheckoutAddCardConfigTest.kt
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
        formConfig: VGSCheckoutPaymentFormConfig = VGSCheckoutPaymentFormConfig(),
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

    override fun describeContents(): Int = 0

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
            formConfig: VGSCheckoutPaymentFormConfig = VGSCheckoutPaymentFormConfig(),
            isScreenshotsAllowed: Boolean = false,
            isRemoveCardOptionEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig>? = null
        ): VGSCheckoutCancellable {
            val config = VGSCheckoutPaymentConfig(
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

            val command = GetSavedCardsCommand(context)
            command.execute(params) {
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
    //endregion

}
