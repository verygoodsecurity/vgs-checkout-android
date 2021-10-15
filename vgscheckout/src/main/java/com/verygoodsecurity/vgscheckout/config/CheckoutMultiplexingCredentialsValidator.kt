package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.util.extension.decodeJwtPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson
import com.verygoodsecurity.vgscheckout.util.extension.toStringList

internal object CheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:"

    private const val RESOURCE_ACCESS_KEY = "resource_access"
    private const val ROLES_KEY = "roles"

    @Throws(IllegalArgumentException::class)
    fun validateJWT(token: String) {
        val payload = token.decodeJwtPayload()?.toJson()
            ?: throw IllegalArgumentException("Can't parse invalid JWT token.")

        val resourceAccess = payload.optJSONObject(RESOURCE_ACCESS_KEY)
        resourceAccess?.keys()?.forEach { key ->
            val roles = resourceAccess.optJSONObject(key)?.optJSONArray(ROLES_KEY)?.toStringList()
            roles?.find { it.contains(RESTRICTED_TOKEN_ROLE_SCOPE) }?.let {
                throw IllegalArgumentException("JWT token contains restricted role [$it].")
            }
        }
    }
}