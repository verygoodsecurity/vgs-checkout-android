package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJWTParseException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJWTRestrictedRoleException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSJdfX19.aYbxWSMXAkKRidfbMRnYVh2haHg03Y9tQUVo4CEuK58"
private const val JWT_WITH_TRANSFERS_WRITE_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSJdfSwibXVsdGlwbGV4aW5nLWFwcC10bnR5eXl5eXkiOnsicm9sZXMiOlsiZmluYW5jaWFsLWluc3RydW1lbnRzOndyaXRlIiwidHJhbnNmZXJzOndyaXRlIl19fX0.TfjhlKwDfmI5B7zByQBou7nmmlVe6TUjyCPBduaVVqM"
private const val JWT_WITH_TRANSFERS_ANY_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSIsInRyYW5zZmVyczphbnkiXX19fQ.Rz7OJSotwXRwOiwPzhAl6hoZn1JqHYA5oXndHDwyfIA"

class CheckoutMultiplexingCredentialsValidatorTest {

    private val jwtValidator = CheckoutMultiplexingCredentialsValidator

    @Test
    fun validateJWT_validJWT_exceptionNotThrown() {
        // Act
        jwtValidator.validateJWT(VALID_JWT)
    }

    @Test(expected = VGSCheckoutJWTParseException::class)
    fun validateJWT_emptyJWT_exceptionThrown() {
        // Act
        jwtValidator.validateJWT("")
    }

    @Test(expected = VGSCheckoutJWTRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersWriteRole_exceptionThrown() {
        // Act
        jwtValidator.validateJWT(JWT_WITH_TRANSFERS_WRITE_ROLE)
    }

    @Test(expected = VGSCheckoutJWTRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersAnyRole_exceptionThrown() {
        // Act
        jwtValidator.validateJWT(JWT_WITH_TRANSFERS_ANY_ROLE)
    }

    @Test
    fun validateJWT_emptyJWT_correctMessage() {
        // Arrange
        val expectedMessage = "Can't parse invalid access token."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJWT("")
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }

    @Test
    fun validateJWT_JWTWithTransfersWriteRole_correctMessage() {
        // Arrange
        val expectedMessage = "Access token contains restricted role [transfers:write]."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJWT(JWT_WITH_TRANSFERS_WRITE_ROLE)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }

    @Test
    fun validateJWT_JWTWithTransfersAnyRole_correctMessage() {
        // Arrange
        val expectedMessage = "Access token contains restricted role [transfers:any]."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJWT(JWT_WITH_TRANSFERS_ANY_ROLE)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }
}