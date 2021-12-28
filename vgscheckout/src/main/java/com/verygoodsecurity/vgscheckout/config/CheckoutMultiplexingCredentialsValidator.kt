package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTParseException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTRestrictedRoleException
import com.verygoodsecurity.vgscheckout.util.extension.decodeJWTPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson
import com.verygoodsecurity.vgscheckout.util.extension.toStringList

internal object CheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:"

    private const val RESOURCE_ACCESS_KEY = "resource_access"
    private const val ROLES_KEY = "roles"

    @Throws(VGSCheckoutException::class)
    fun validateJWT(token: String) {
        val payload = token.decodeJWTPayload()?.toJson() ?: throw VGSCheckoutJWTParseException()

        val resourceAccess = payload.optJSONObject(RESOURCE_ACCESS_KEY)

        resourceAccess?.keys()?.forEach { key ->
            val roles = resourceAccess.optJSONObject(key)?.optJSONArray(ROLES_KEY)?.toStringList()
            roles?.find { it.contains(RESTRICTED_TOKEN_ROLE_SCOPE) }?.let {
                throw VGSCheckoutJWTRestrictedRoleException(it)
            }
        }
    }
}