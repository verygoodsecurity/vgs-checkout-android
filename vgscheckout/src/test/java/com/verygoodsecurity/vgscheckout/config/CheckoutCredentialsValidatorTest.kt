package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTParseException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTRestrictedRoleException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CheckoutCredentialsValidatorTest {

    private val jwtValidator = CheckoutCredentialsValidator

    @Test
    fun validateJWT_validJWT_exceptionNotThrown() {
        // Act
        jwtValidator.validateJWT(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
    }

    @Test(expected = VGSCheckoutJWTParseException::class)
    fun validateJWT_emptyJWT_exceptionThrown() {
        // Act
        jwtValidator.validateJWT("")
    }
    @Test(expected = VGSCheckoutJWTRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersWriteRole_exceptionThrown() {
        // Act
        jwtValidator.validateJWT(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_WRITE)
    }

    @Test(expected = VGSCheckoutJWTRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersAnyRole_exceptionThrown() {
        // Act
        jwtValidator.validateJWT(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_ANY)
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
            jwtValidator.validateJWT(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_WRITE)
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
            jwtValidator.validateJWT(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_ANY)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }
}