package com.verygoodsecurity.vgscheckout.config

import android.os.Parcel
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.JWTValidationEvent
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
    override val isAnalyticsEnabled: Boolean,
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
        environment: String = DEFAULT_ENVIRONMENT,
        isAnalyticsEnabled: Boolean = true
    ) : this(
        token,
        vaultID,
        environment,
        getRouteConfiguration(token),
        getFormConfig(),
        isAnalyticsEnabled,
        false
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
        parcel.writeString(vaultID)
        parcel.writeString(environment)
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
            CheckoutMultiplexingCredentialsValidator.validateJWT(vaultID, token)
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

        private const val CARD_NUMBER_FIELD_NAME = "card.number"
        private const val CARD_HOLDER_FIELD_NAME = "card.name"
        private const val CVC_FIELD_NAME = "card.cvc"
        private const val EXPIRY_DATE_FIELD_NAME = "card.expDate"
        private const val MONTH_FIELD_NAME = "card.exp_month"
        private const val YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/YYYY"

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

        private fun getFormConfig(): VGSCheckoutFormConfiguration {
            return VGSCheckoutFormConfiguration(
                cardOptions = VGSCheckoutCardOptions(
                    VGSCheckoutCardNumberOptions(CARD_NUMBER_FIELD_NAME),
                    VGSCheckoutCardHolderOptions(CARD_HOLDER_FIELD_NAME),
                    VGSCheckoutCVCOptions(CVC_FIELD_NAME),
                    VGSCheckoutExpirationDateOptions(
                        EXPIRY_DATE_FIELD_NAME,
                        VGSDateSeparateSerializer(
                            MONTH_FIELD_NAME,
                            YEAR_FIELD_NAME
                        ),
                        outputFormatRegex = EXPIRY_DATE_OUTPUT_FORMAT
                    )
                )
            )
        }
    }
}