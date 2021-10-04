package com.verygoodsecurity.vgscheckout.config

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

private const val VAULT_ID = "tntxxxxxxx"
private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiXX19fQ.-zjySsFy3G3cy-wkw1VpHRUS10OmKlI_rElg_4_3lsw"
private const val JWT_WITH_TRANSFERS_WRITE_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiLCJ0cmFuc2ZlcnM6d3JpdGUiXX19fQ.4MLpBR9-zgVAulInJq4iHLL_c7A79v_b-W7BWjr2fJU"
private const val JWT_WITH_TRANSFERS_ANY_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiLCJ0cmFuc2ZlcnM6YW55Il19fX0.4YTKip8FIZ2VFfPlKFXSw6LMqoOGe2ai8eOH8QXfSxU"
private const val EMPTY_JWT = ""

// TODO: Uncomment tests before release

class CheckoutMultiplexingCredentialsValidatorTest {

    private val jwtValidator = CheckoutMultiplexingCredentialsValidator

    @Test
    fun validateJWT_validJWT_tokenReturned() {
        // Act
        val result = jwtValidator.validateJWT(VAULT_ID, VALID_JWT)
        // Assert
        assertEquals(VALID_JWT, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun validateJWT_emptyJWT_exceptionThrown() {
        // Act
        jwtValidator.validateJWT(VAULT_ID, EMPTY_JWT)
    }

//    @Test(expected = IllegalArgumentException::class)
//    fun validateJWT_JWTWithTransfersWriteRole_exceptionThrown() {
//        // Act
//        jwtValidator.validateJWT(VAULT_ID, JWT_WITH_TRANSFERS_WRITE_ROLE)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun validateJWT_JWTWithTransfersAnyRole_exceptionThrown() {
//        // Act
//        jwtValidator.validateJWT(VAULT_ID, JWT_WITH_TRANSFERS_ANY_ROLE)
//    }

    @Test
    fun validateJWT_emptyJWT_correctMessage() {
        // Arrange
        val expectedMessage = "Can't parse invalid JWT token."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJWT(VAULT_ID, EMPTY_JWT)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }

//    @Test
//    fun validateJWT_JWTWithTransfersWriteRole_correctMessage() {
//        // Arrange
//        val expectedMessage = "JWT token contains restricted role [transfers:write]."
//        var exception: Exception? = null
//        // Act
//        try {
//            jwtValidator.validateJWT(VAULT_ID, JWT_WITH_TRANSFERS_WRITE_ROLE)
//        } catch (e: Exception) {
//            exception = e
//        }
//        // Assert
//        assertNotNull(exception)
//        assertEquals(expectedMessage, exception?.message)
//    }
//
//    @Test
//    fun validateJWT_JWTWithTransfersAnyRole_correctMessage() {
//        // Arrange
//        val expectedMessage = "JWT token contains restricted role [transfers:any]."
//        var exception: Exception? = null
//        // Act
//        try {
//            jwtValidator.validateJWT(VAULT_ID, JWT_WITH_TRANSFERS_ANY_ROLE)
//        } catch (e: Exception) {
//            exception = e
//        }
//        // Assert
//        assertNotNull(exception)
//        assertEquals(expectedMessage, exception?.message)
//    }
}