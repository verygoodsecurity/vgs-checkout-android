package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.core.DEFAULT_ENVIRONMENT
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.decodeJwtPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingConfiguration private constructor(
    internal val token: String,
    override val vaultID: String,
    override val environment: String,
    override val routeConfig: VGSCheckoutRouteConfiguration,
    override val formConfig: VGSCheckoutMultiplexingFormConfiguration
) : CheckoutConfiguration() {

    /**
     * @throws IllegalArgumentException if token is not valid.
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    constructor(
        vaultID: String,
        token: String,
        environment: String = DEFAULT_ENVIRONMENT
    ) : this(
        VGSCheckoutMultiplexingCredentialsValidator.validateJWT(vaultID, token),
        vaultID,
        environment,
        getVGSCheckoutRouteConfiguration(token),
        VGSCheckoutMultiplexingFormConfiguration()
    )

    companion object {
        private fun getVGSCheckoutRouteConfiguration(token: String) =
            VGSCheckoutRouteConfiguration(
                "/financial_instruments",
                requestOptions = VGSCheckoutRequestOptions(
                    extraHeaders = mapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to "Bearer $token"
                    )
                )
            )
    }
}

internal object VGSCheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:write"

    private const val RESOURCE_ACCESS_KEY = "resource_access"
    private const val APP_ID_KEY_PREFIX = "multiplexing-app-"
    private const val ROLES_KEY = "roles"

    @Throws(IllegalArgumentException::class)
    fun validateJWT(vaultID: String, token: String): String {
        val payload = token.decodeJwtPayload()?.toJson()
            ?: throw IllegalArgumentException("Can't parse invalid JWT token.")

        val roles = payload.optJSONObject(RESOURCE_ACCESS_KEY)
            ?.optJSONObject(APP_ID_KEY_PREFIX + vaultID)
            ?.optJSONArray(ROLES_KEY)
            ?: throw IllegalArgumentException("JWT token doesn't contains roles.")

        //todo uncomment before release or testing
//        if (roles.contains(RESTRICTED_TOKEN_ROLE_SCOPE)) {
//            throw IllegalArgumentException("JWT token contains restricted role [$RESTRICTED_TOKEN_ROLE_SCOPE].")
//        }

        return token
    }
}