package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException
import com.verygoodsecurity.vgscheckout.util.extension.decodeJwtPayload
import com.verygoodsecurity.vgscheckout.util.extension.toJson
import com.verygoodsecurity.vgscheckout.util.extension.toStringList

internal object CheckoutMultiplexingCredentialsValidator {

    private const val RESTRICTED_TOKEN_ROLE_SCOPE = "transfers:"

    private const val RESOURCE_ACCESS_KEY = "resource_access"
    private const val ROLES_KEY = "roles"

    @Throws(VGSCheckoutInvalidJwtException::class, VGSCheckoutJwtRestrictedRoleException::class)
    fun validateJwt(token: String) {
        val payload = token.decodeJwtPayload()?.toJson() ?: throw VGSCheckoutInvalidJwtException()

        val resourceAccess = payload.optJSONObject(RESOURCE_ACCESS_KEY)

        resourceAccess?.keys()?.forEach { key ->
            val roles = resourceAccess.optJSONObject(key)?.optJSONArray(ROLES_KEY)?.toStringList()
            roles?.find { it.contains(RESTRICTED_TOKEN_ROLE_SCOPE) }?.let {
                throw VGSCheckoutJwtRestrictedRoleException(it)
            }
        }
    }
}