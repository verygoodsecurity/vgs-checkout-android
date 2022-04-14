package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.collect.core.analytic.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutPaymentFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutCard
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.command.order.GetPaymentInfo
import com.verygoodsecurity.vgscheckout.util.command.order.PaymentInfo
import java.util.*

/**
 * Holds configuration with predefined setup for work with payment orchestration app.
 *
 * @param accessToken payment orchestration app access token.
 * @param tenantId payment orchestration tenant id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
 * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues if any occurs.
 * @param savedCards list of previously saved cards.
 * @param createdFromParcel if true then object created form parcel. Used to determine if access token
 * validation event should be send.
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
internal class VGSCheckoutPaymentConfig private constructor(
    internal val accessToken: String,
    val tenantId: String,
    internal val paymentInfo: PaymentInfo,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: VGSCheckoutPaymentFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isAnalyticsEnabled: Boolean,
    internal val savedCards: List<VGSCheckoutCard>,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

    init {
        if (!createdFromParcel) validateToken()
    }

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(PaymentInfo::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutPaymentRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutAddCardFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        LinkedList<VGSCheckoutCard>().apply {
            parcel.readList(
                this,
                VGSCheckoutCard::class.java.classLoader
            )
        },
        true
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(tenantId)
        parcel.writeParcelable(paymentInfo, flags)
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

    @Throws(VGSCheckoutException::class)
    private fun validateToken() {
        try {
            CheckoutCredentialsValidator.validateJWT(accessToken)
            analyticTracker.log(JWTValidationEvent(true))
        } finally {
            analyticTracker.log(JWTValidationEvent(false))
        }
    }

    companion object CREATOR : Parcelable.Creator<VGSCheckoutPaymentConfig> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutPaymentConfig {
            return VGSCheckoutPaymentConfig(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutPaymentConfig?> {
            return arrayOfNulls(size)
        }

        /**
         * Check all parameters and create [VGSCheckoutPaymentConfig] asynchronously.
         *
         * @param accessToken payment orchestration app access token.
         * @param orderId id of order created on server.
         * @param tenantId payment orchestration tenant id.
         * @param callback initialization callback.
         * @param environment type of vault.
         * @param formConfig UI configuration.
         * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
         * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues if any occurs.
         *
         * @return [VGSCheckoutCancellable]
         */
        @JvmStatic
        @JvmOverloads
        fun create(
            accessToken: String,
            orderId: String,
            tenantId: String,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutPaymentFormConfig = VGSCheckoutPaymentFormConfig(),
            isScreenshotsAllowed: Boolean = false,
            isAnalyticsEnabled: Boolean = true,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutPaymentConfig>? = null,
        ): VGSCheckoutCancellable = GetPaymentInfo().execute(orderId) {
            try {
                when (it) {
                    is Result.Success<PaymentInfo> -> {
                        callback?.onSuccess(
                            VGSCheckoutPaymentConfig(
                                accessToken,
                                tenantId,
                                it.data,
                                environment,
                                VGSCheckoutPaymentRouteConfig(accessToken),
                                formConfig,
                                isScreenshotsAllowed,
                                isAnalyticsEnabled,
                                getCardsMock(),
                                false
                            )
                        )
                    }
                    is Result.Error -> callback?.onFailure(it.e)
                }
            } catch (e: VGSCheckoutException) {
                callback?.onFailure(e)
            }
        }


        private fun getCardsMock(): List<VGSCheckoutCard> {
            val brands = VGSCheckoutCardBrand.BRANDS.toList()
            val result = mutableListOf<VGSCheckoutCard>()
            for (i in 0 until 10) {
                result.add(
                    VGSCheckoutCard(
                        UUID.randomUUID().toString(),
                        "Test $i",
                        "$i$i$i$i",
                        "09/24",
                        brands.random().name
                    )
                )
            }
            return result
        }
    }
}