package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutMultiplexingRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJWTParseException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJWTRestrictedRoleException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.order.GetOrderDetails
import com.verygoodsecurity.vgscheckout.util.command.VGSCancellable
import com.verygoodsecurity.vgscheckout.util.command.order.OrderDetails

/**
 * Holds configuration with predefined setup for work with payment orchestration/multiplexing app.
 *
 * @param accessToken multiplexing app access token.
 * @param tenantId payment orchestration tenant id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
 * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues if any occurs.
 * @param createdFromParcel if true then object created form parcel. Used to determine if access token
 * validation event should be send.
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutMultiplexingPaymentConfig private constructor(
    internal val accessToken: String,
    val tenantId: String,
    internal val orderDetails: OrderDetails,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutMultiplexingRouteConfig,
    override val formConfig: VGSCheckoutMultiplexingFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isAnalyticsEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

    init {
        if (!createdFromParcel) validateToken()
    }

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(OrderDetails::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        true
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(tenantId)
        parcel.writeParcelable(orderDetails, flags)
        parcel.writeParcelable(environment, flags)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
        parcel.writeInt(if (isScreenshotsAllowed) 1 else 0)
        parcel.writeInt(if (isAnalyticsEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Throws(VGSCheckoutJWTParseException::class, VGSCheckoutJWTRestrictedRoleException::class)
    private fun validateToken() {
        try {
            CheckoutMultiplexingCredentialsValidator.validateJWT(accessToken)
            analyticTracker.log(JWTValidationEvent(true))
        } finally {
            analyticTracker.log(JWTValidationEvent(false))
        }
    }

    companion object CREATOR : Parcelable.Creator<VGSCheckoutMultiplexingPaymentConfig> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutMultiplexingPaymentConfig {
            return VGSCheckoutMultiplexingPaymentConfig(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutMultiplexingPaymentConfig?> {
            return arrayOfNulls(size)
        }

        @JvmStatic
        @JvmOverloads
        fun create(
            accessToken: String,
            tenantId: String,
            orderId: String,
            callback: VGSCheckoutConfigInitCallback<VGSCheckoutMultiplexingPaymentConfig>,
            environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
            formConfig: VGSCheckoutMultiplexingFormConfig = VGSCheckoutMultiplexingFormConfig(),
            isScreenshotsAllowed: Boolean = false,
            isAnalyticsEnabled: Boolean = true,
        ): VGSCancellable = GetOrderDetails().execute(orderId) {
            try {
                when(it) {
                    is Result.Success<OrderDetails> -> {
                        callback.onSuccess(
                            VGSCheckoutMultiplexingPaymentConfig(
                                accessToken,
                                tenantId,
                                it.data,
                                environment,
                                VGSCheckoutMultiplexingRouteConfig(accessToken),
                                formConfig,
                                isScreenshotsAllowed,
                                isAnalyticsEnabled,
                                false
                            )
                        )
                    }
                    is Result.Error -> callback.onFailure(it.e)
                }
            } catch (e: VGSCheckoutException) {
                callback.onFailure(e)
            }
        }
    }
}