package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JWTValidation
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class VGSCheckoutMultiplexingConfiguration private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: String,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutFormConfiguration,
    private val createdFromParcel: Boolean
) : CheckoutConfiguration() {

    init {

        if (!createdFromParcel) validateToken()
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(VGSCheckoutRouteConfiguration::class.java.classLoader)!!,
        parcel.readParcelable(VGSCheckoutFormConfiguration::class.java.classLoader)!!,
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
        environment: String = DEFAULT_ENVIRONMENT
    ) : this(
        token,
        vaultID,
        environment,
        getRouteConfiguration(token),
        getFormConfig(),
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeString(vaultID)
        parcel.writeString(environment)
        parcel.writeParcelable(routeConfig, flags)
        parcel.writeParcelable(formConfig, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


    @Throws(IllegalArgumentException::class)
    private fun validateToken() {
        try {
            CheckoutMultiplexingCredentialsValidator.validateJWT(vaultID, token)
            analyticTracker.log(JWTValidation(true))
        } catch (e: IllegalArgumentException) {
            analyticTracker.log(JWTValidation(false))
            throw e
        }
    }

    companion object CREATOR : Parcelable.Creator<VGSCheckoutMultiplexingConfiguration> {

        override fun createFromParcel(parcel: Parcel): VGSCheckoutMultiplexingConfiguration {
            return VGSCheckoutMultiplexingConfiguration(parcel)
        }

        override fun newArray(size: Int): Array<VGSCheckoutMultiplexingConfiguration?> {
            return arrayOfNulls(size)
        }

        private fun getRouteConfiguration(token: String): VGSCheckoutRouteConfiguration {
            return VGSCheckoutRouteConfiguration(
                "/financial_instruments",
                requestOptions = VGSCheckoutRequestOptions(
                    extraHeaders = mapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to "Bearer $token"
                    )
                )
            )
        }

        private fun getFormConfig(): VGSCheckoutFormConfiguration {
            return VGSCheckoutFormConfiguration(
                cardOptions = VGSCheckoutCardOptions(
                    VGSCheckoutCardNumberOptions("card.number"),
                    VGSCheckoutCardHolderOptions("card.name"),
                    VGSCheckoutCVCOptions("card.cvc"),
                    VGSCheckoutExpirationDateOptions(
                        "card.expDate",
                        VGSDateSeparateSerializer(
                            "card.exp_month",
                            "card.exp_year"
                        ),
                        outputFormatRegex = "MM/YYYY"
                    )
                )
            )
        }
    }
}