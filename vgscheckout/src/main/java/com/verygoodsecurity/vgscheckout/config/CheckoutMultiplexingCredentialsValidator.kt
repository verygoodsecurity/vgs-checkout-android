package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.util.extension.decodeJwtPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson
import com.verygoodsecurity.vgscheckout.util.extension.toStringList

internal object CheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:"

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
            ?.toStringList()

//        roles?.find { it.contains(RESTRICTED_TOKEN_ROLE_SCOPE) }?.let {
//            throw IllegalArgumentException("JWT token contains restricted role [$it].")
//        }

        return token
    }
}