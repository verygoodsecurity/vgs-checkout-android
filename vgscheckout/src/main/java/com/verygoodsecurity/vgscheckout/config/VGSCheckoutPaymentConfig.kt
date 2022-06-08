package com.verygoodsecurity.vgscheckout.config

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.analytic.event.FinInstrumentCrudEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.payment.OrderDetails
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutPaymentFormConfig
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.networking.command.GetOrderDetails
import com.verygoodsecurity.vgscheckout.networking.command.GetSavedCardsCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.CompositeCommand
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl

class VGSCheckoutPaymentConfig internal constructor(
    internal val accessToken: String,
    internal val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutPaymentFormConfig,
    override val isScreenshotsAllowed: Boolean,
    private val isRemoveCardOptionEnabled: Boolean,
    private val createdFromParcel: Boolean,
    internal var orderDetails: OrderDetails? = null
) : CheckoutConfig(tenantId) {

    internal var savedCards: List<Card> = emptyList()
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) internal set

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
        true,
        parcel.readParcelable(VGSCheckoutAddCardFormConfig::class.java.classLoader)!!,
    ) {
        this.savedCards = mutableListOf<Card>().apply {
            parcel.readList(
                this,
                Card::class.java.classLoader
            )
        }
    }

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
        parcel.writeParcelable(orderDetails, flags)
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
         * @param orderId id of order.
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
            orderId: String,
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

            val saveCardCommand = with(config) {
                GetSavedCardsCommand(
                    context,
                    GetSavedCardsCommand.Params(
                        getBaseUrl(context),
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
                        getBaseUrl(context),
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
                        is GetSavedCardsCommand.Result -> saveCardsDetails(config, ids, result)
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
            ids: List<String>,
            result: GetSavedCardsCommand.Result
        ) {
            when (result) {
                is GetSavedCardsCommand.Result.Success -> {
                    config.analyticTracker.log(
                        FinInstrumentCrudEvent.load(
                            FinInstrumentCrudEvent.DEFAULT_CODE,
                            true,
                            null,
                            false,
                            ids.count(),
                            ids.count() - result.cards.count()
                        )
                    )
                    config.savedCards = result.cards
                }
                is GetSavedCardsCommand.Result.Failure -> {
                    config.analyticTracker.log(
                        FinInstrumentCrudEvent.load(
                            result.exception.code,
                            false,
                            result.exception.message,
                            false,
                            ids.count(),
                            ids.count(),
                        )
                    )
                }
            }
        }
    }
}
