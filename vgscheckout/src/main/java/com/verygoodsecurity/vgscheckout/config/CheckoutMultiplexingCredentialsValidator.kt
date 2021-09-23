package com.verygoodsecurity.vgscheckout.config

import android.util.Log
import com.verygoodsecurity.vgscheckout.util.extension.decodeJwtPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson

internal object CheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:write"

    private const val RESOURCE_ACCESS_KEY = "resource_access"
    private const val APP_ID_KEY_PREFIX = "multiplexing-app-"
    private const val ROLES_KEY = "roles"

    @Throws(IllegalArgumentException::class)
    fun validateJWT(vaultID: String, token: String): String {
        Log.e("test", "$vaultID - $token")
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