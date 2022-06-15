package com.verygoodsecurity.vgscheckout.config.core

import com.verygoodsecurity.vgscheckout.analytic.event.JWTValidationEvent
import com.verygoodsecurity.vgscheckout.config.CheckoutCredentialsValidator
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment

abstract class OrchestrationConfig internal constructor(
    internal val accessToken: String,
    val tenantId: String,
    override val environment: VGSCheckoutEnvironment,
    override val routeConfig: VGSCheckoutPaymentRouteConfig,
    override val formConfig: CheckoutFormConfig,
    override val isScreenshotsAllowed: Boolean,
    val isRemoveCardOptionEnabled: Boolean,
    private val createdFromParcel: Boolean
) : CheckoutConfig(tenantId) {

    internal var savedCards: List<Card> = emptyList()

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