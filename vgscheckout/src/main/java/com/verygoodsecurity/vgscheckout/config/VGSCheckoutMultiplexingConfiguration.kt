package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutMultiplexingConfiguration private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutMultiplexingFormConfiguration,
    override val isAnalyticsEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfiguration() {

    init {

        if (!createdFromParcel) validateToken()
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutRouteConfiguration::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutFormConfiguration::class.java.classLoader)!!,
        parcel.readInt() == 1,
        true
    )

    /**
     * @throws IllegalArgumentException if token is not valid.
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    constructor(
        token: String,
        vaultID: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        formConfig: VGSCheckoutMultiplexingFormConfiguration = VGSCheckoutMultiplexingFormConfiguration(),
        isAnalyticsEnabled: Boolean = true
    ) : this(
        token,
        vaultID,
        environment,
        getRouteConfiguration(token),
        formConfig,
        isAnalyticsEnabled,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeString(vaultID)
        parcel.writeParcelable(environment, flags)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
        parcel.writeInt(if (isAnalyticsEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    private fun validateToken() {
        try {
//            CheckoutMultiplexingCredentialsValidator.validateJWT(vaultID, token)
            analyticTracker.log(JWTValidationEvent(true))
        } finally {
            analyticTracker.log(JWTValidationEvent(false))
        }
    }

    companion object CREATOR : Parcelable.Creator<VGSCheckoutMultiplexingConfiguration> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutMultiplexingConfiguration {
            return VGSCheckoutMultiplexingConfiguration(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutMultiplexingConfiguration?> {
            return arrayOfNulls(size)
        }

        private const val PATH = "/financial_instruments"
        private const val CONTENT_TYPE_HEADER_NAME = "Content-Type"
        private const val CONTENT_TYPE = "application/json"
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_TYPE = "Bearer"

        private fun getRouteConfiguration(token: String): VGSCheckoutRouteConfiguration {
            return VGSCheckoutRouteConfiguration(
                PATH,
                requestOptions = VGSCheckoutRequestOptions(
                    extraHeaders = mapOf(
                        CONTENT_TYPE_HEADER_NAME to CONTENT_TYPE,
                        AUTHORIZATION_HEADER_NAME to "$BEARER_TOKEN_TYPE $token"
                    )
                )
            )
        }
    }
}