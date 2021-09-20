package com.verygoodsecurity.vgscheckout.config

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
import kotlinx.parcelize.Parcelize

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
@Parcelize
class VGSCheckoutMultiplexingConfiguration private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: String,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutFormConfiguration
) : CheckoutConfiguration() {

    init {

        try {
            CheckoutMultiplexingCredentialsValidator.validateJWT(vaultID, token)
            // TODO: Log JWT validation event
        } catch (e: IllegalArgumentException) {
            // TODO: Log JWT validation event
            throw e
        }
    }

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
        getFormConfig()
    )

    companion object {

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