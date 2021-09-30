package com.verygoodsecurity.vgscheckout.config

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

private const val VAULT_ID = "tntxxxxxxx"
private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiXX19fQ.-zjySsFy3G3cy-wkw1VpHRUS10OmKlI_rElg_4_3lsw"
private const val JWT_WITHOUT_ROLES =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6IlRFU1QifX0.WmsmpplJ9G6IW-D9RaZV9XS4yuXw6-7k0c28bEP7YCY"
private const val JWT_WITH_RESTRICTED_ROLES =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiLCJ0cmFuc2ZlcnM6d3JpdGUiXX19fQ.4MLpBR9-zgVAulInJq4iHLL_c7A79v_b-W7BWjr2fJU"
private const val EMPTY_JWT = ""

class CheckoutMultiplexingCredentialsValidatorTest {

    @Test
    fun createMultiplexingConfig_validJWT_objectCreated() {
        // Act
        val config = VGSCheckoutMultiplexingConfiguration(VALID_JWT, VAULT_ID)
        // Assert
        assertNotNull(config)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createMultiplexingConfig_emptyJWT_exceptionThrown() {
        // Act
        VGSCheckoutMultiplexingConfiguration(EMPTY_JWT, VAULT_ID)
    }

    @Test
    fun createMultiplexingConfig_emptyJWT_correctMessage() {
        // Arrange
        val expectedMessage = "Can't parse invalid JWT token."
        var exception: Exception? = null
        // Act
        try {
            VGSCheckoutMultiplexingConfiguration(EMPTY_JWT, VAULT_ID)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createMultiplexingConfig_JWTWithoutRoles_exceptionThrown() {
        // Act
        VGSCheckoutMultiplexingConfiguration(JWT_WITHOUT_ROLES, VAULT_ID)
    }

    @Test
    fun createMultiplexingConfig_JWTWithoutRoles_correctMessage() {
        // Arrange
        val expectedMessage = "JWT token doesn't contains roles."
        var exception: Exception? = null
        // Act
        try {
            VGSCheckoutMultiplexingConfiguration(JWT_WITHOUT_ROLES, VAULT_ID)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }

//    TODO: Uncomment before public release
//    @Test(expected = IllegalArgumentException::class)
//    fun createMultiplexingConfig_JWTWithRestrictedRole_exceptionThrown() {
//        // Act
//        VGSCheckoutMultiplexingConfiguration(JWT_WITH_RESTRICTED_ROLES, VAULT_ID)
//    }
//
//    @Test
//    fun createMultiplexingConfig_JWTWithRestrictedRole_correctMessage() {
//        // Arrange
//        val expectedMessage = "JWT token contains restricted role [transfers:write]."
//        var exception: Exception? = null
//        // Act
//        try {
//            VGSCheckoutMultiplexingConfiguration(JWT_WITH_RESTRICTED_ROLES, VAULT_ID)
//        } catch (e: Exception) {
//            exception = e
//        }
//        // Assert
//        assertNotNull(exception)
//        assertEquals(expectedMessage, exception?.message)
//    }
}