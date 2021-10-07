package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutMultiplexingRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfig
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutMultiplexingConfig private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutMultiplexingRouteConfig,
    override val formConfig: VGSCheckoutMultiplexingFormConfig,
    override val isAnalyticsEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig() {

    init {

        if (!createdFromParcel) validateToken()
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutEnvironment::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingRouteConfig::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutMultiplexingFormConfig::class.java.classLoader)!!,
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
        formConfig: VGSCheckoutMultiplexingFormConfig = VGSCheckoutMultiplexingFormConfig(),
        isAnalyticsEnabled: Boolean = true
    ) : this(
        token,
        vaultID,
        environment,
        VGSCheckoutMultiplexingRouteConfig(token),
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
            CheckoutMultiplexingCredentialsValidator.validateJWT(token)
            analyticTracker.log(JWTValidationEvent(true))
        } finally {
            analyticTracker.log(JWTValidationEvent(false))
        }
    }

    companion object CREATOR : Parcelable.Creator<VGSCheckoutMultiplexingConfig> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutMultiplexingConfig {
            return VGSCheckoutMultiplexingConfig(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutMultiplexingConfig?> {
            return arrayOfNulls(size)
        }
    }
}