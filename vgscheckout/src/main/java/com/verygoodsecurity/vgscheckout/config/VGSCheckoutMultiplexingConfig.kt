package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JwtValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutMultiplexingRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

/**
 * Holds configuration with predefined setup for work with payment orchestration/multiplexing app.
 *
 * @param token multiplexing app access token.
 * @param vaultID unique organization vault id.
 * @param environment type of vault.
 * @param routeConfig Networking configuration, like http method, request headers etc.
 * @param formConfig UI configuration.
 * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots.
 * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug issues if any occurs.
 * @param createdFromParcel if true then object created form parcel. Used to determine if token
 * validation event should be send.
 */
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutMultiplexingConfig private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutMultiplexingRouteConfig,
    override val formConfig: VGSCheckoutMultiplexingFormConfig,
    override val isScreenshotsAllowed: Boolean,
    override val isAnalyticsEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig() {

    init {

        if (!createdFromParcel) validateToken()
    }

    internal constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingFormConfig::class.java.classLoader)!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        true
    )

    /**
     * Public constructor.
     *
     * @param token multiplexing app access token.
     * @param vaultID unique organization vault id.
     * @param environment type of vault.
     * @param formConfig UI configuration.
     * @param isScreenshotsAllowed If true, checkout form will allow to make screenshots. Default is false.
     * @param isAnalyticsEnabled If true, checkout will send analytics events that helps to debug
     * issues if any occurs. Default value is true.
     *
     * @throws com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException if token is not valid.
     * @throws com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException if
     * token is contains restricted roles.
     */
    @JvmOverloads
    @Throws(VGSCheckoutInvalidJwtException::class, VGSCheckoutJwtRestrictedRoleException::class)
    constructor(
        token: String,
        vaultID: String,
        environment: VGSCheckoutEnvironment = VGSCheckoutEnvironment.Sandbox(),
        formConfig: VGSCheckoutMultiplexingFormConfig = VGSCheckoutMultiplexingFormConfig(),
        isScreenshotsAllowed: Boolean = false,
        isAnalyticsEnabled: Boolean = true
    ) : this(
        token,
        vaultID,
        environment,
        VGSCheckoutMultiplexingRouteConfig(token),
        formConfig,
        isScreenshotsAllowed,
        isAnalyticsEnabled,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeString(vaultID)
        parcel.writeParcelable(environment, flags)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
        parcel.writeInt(if (isScreenshotsAllowed) 1 else 0)
        parcel.writeInt(if (isAnalyticsEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Throws(IllegalArgumentException::class)
    private fun validateToken() {
        try {
            CheckoutMultiplexingCredentialsValidator.validateJwt(token)
            analyticTracker.log(JwtValidationEvent(true))
        } finally {
            analyticTracker.log(JwtValidationEvent(false))
        }
    }

    internal companion object CREATOR : Parcelable.Creator<VGSCheckoutMultiplexingConfig> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutMultiplexingConfig {
            return VGSCheckoutMultiplexingConfig(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutMultiplexingConfig?> {
            return arrayOfNulls(size)
        }
    }
}