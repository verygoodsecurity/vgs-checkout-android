package com.verygoodsecurity.vgscheckout.config.core

import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.analytic.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.CheckoutCredentialsValidator
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

abstract class OrchestrationConfig internal constructor(
    internal open val accessToken: String,
    override val routeId: String = PAYMENT_URL_ROUTE_ID,
    override val id: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutRouteConfig,
    override val formConfig: VGSCheckoutFormConfig,
    override val isScreenshotsAllowed: Boolean,
    open val isRemoveCardOptionEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig() {

    internal var savedCards: List<Card> = emptyList()
        @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED) internal set

    init {
        //TODO: Uncomment token validation
        //TODO: Uncomment tests in VGSCheckoutAddCardConfigTest.kt
//        if (!createdFromParcel) validateToken()
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
        internal const val PAYMENT_URL_ROUTE_ID = "4880868f-d88b-4333-ab70-d9deecdbffc4"

        private const val CARD_NUMBER_FIELD_NAME = "card.number"
        private const val CARD_HOLDER_FIELD_NAME = "card.name"
        private const val CVC_FIELD_NAME = "card.cvc"
        private const val EXPIRY_FIELD_NAME = "card.expDate"
        private const val EXPIRY_MONTH_FIELD_NAME = "card.exp_month"
        private const val EXPIRY_YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_INPUT_FORMAT = "MM/yy"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/yyyy"

        internal const val COUNTRY_FIELD_NAME = "card.billing_address.country"
        internal const val CITY_FIELD_NAME = "card.billing_address.city"
        internal const val ADDRESS_FIELD_NAME = "card.billing_address.address1"
        internal const val OPTIONAL_FIELD_NAME = "card.billing_address.address2"
        internal const val POSTAL_CODE_FIELD_NAME = "card.billing_address.postal_code"


        internal fun createCardOptions() = VGSCheckoutCardOptions(
            VGSCheckoutCardNumberOptions(
                CARD_NUMBER_FIELD_NAME,
                false,
                VGSCheckoutCardBrand.BRANDS
            ),
            VGSCheckoutCardHolderOptions(
                CARD_HOLDER_FIELD_NAME,
                VGSCheckoutFieldVisibility.VISIBLE
            ),
            VGSCheckoutCVCOptions(
                CVC_FIELD_NAME,
                false
            ),
            VGSCheckoutExpirationDateOptions(
                EXPIRY_FIELD_NAME,
                VGSDateSeparateSerializer(EXPIRY_MONTH_FIELD_NAME, EXPIRY_YEAR_FIELD_NAME),
                EXPIRY_DATE_INPUT_FORMAT,
                EXPIRY_DATE_OUTPUT_FORMAT
            )
        )

        //todo think if we can remove because we use different api for orders, transfers, etc.
        private const val PATH = "/financial_instruments"

        private const val CONTENT_TYPE_HEADER_NAME = "Content-Type"
        private const val CONTENT_TYPE = "application/json"

        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_TYPE = "Bearer"

        internal fun createRouteConfig(accessToken: String) = VGSCheckoutRouteConfig(
            PATH,
            VGSCheckoutHostnamePolicy.Vault,
            VGSCheckoutRequestOptions(
                VGSCheckoutHttpMethod.POST,
                mapOf(
                    CONTENT_TYPE_HEADER_NAME to CONTENT_TYPE,
                    AUTHORIZATION_HEADER_NAME to "$BEARER_TOKEN_TYPE $accessToken"
                ),
                emptyMap(),
                VGSCheckoutDataMergePolicy.NESTED_JSON
            )
        )
    }
}