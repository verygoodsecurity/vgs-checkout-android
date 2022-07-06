package com.verygoodsecurity.vgscheckout.config.core

import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.analytic.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.CheckoutCredentialsValidator
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import kotlinx.parcelize.IgnoredOnParcel

abstract class OrchestrationConfig internal constructor(
    internal open val accessToken: String,
    open val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: CheckoutFormConfig,
    override val isScreenshotsAllowed: Boolean,
    open val isRemoveCardOptionEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

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
}