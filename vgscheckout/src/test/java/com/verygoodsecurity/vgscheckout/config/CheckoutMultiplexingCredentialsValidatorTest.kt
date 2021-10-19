package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiXX19fQ.-zjySsFy3G3cy-wkw1VpHRUS10OmKlI_rElg_4_3lsw"
private const val JWT_WITH_TRANSFERS_WRITE_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA1MjIyODcsImlhdCI6MTYzMDUyMTk4NywianRpIjoiNWVjZGVyMzItOWM2My00YzAyLWE1MjMtYWE4MjRjYzI3NDRjIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLnZlcnlnb29kc2VjdXJpdHkuY29tL2F1dGgvcmVhbG1zL3ZncyIsImF1ZCI6Im11bHRpcGxleGluZy1hcHAtdG50c2htbGpsYTciLCJzdWIiOiI3ZmZmZDFkZS1hM2UyLTRkMDAtYjAzZi00Mjg4MjJhMmFlOWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwic2Vzc2lvbl9zdGF0ZSI6IjQzMTJkODcyLTNmZWUtNDlhMy05OGNmLTc1YjAwMDA5N2U0NiIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSJdfSwibXVsdGlwbGV4aW5nLWFwcC10bnR5eXl5eXkiOnsicm9sZXMiOlsiZmluYW5jaWFsLWluc3RydW1lbnRzOndyaXRlIiwidHJhbnNmZXJzOndyaXRlIl19fSwic2NvcGUiOiJ1c2VyX2lkIHNlcnZpY2UtYWNjb3VudCIsInNlcnZpY2VfYWNjb3VudCI6dHJ1ZSwiY2xpZW50SWQiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwiY2xpZW50SG9zdCI6IjE3Ni4xMDAuMTA1LjIzNSIsImNsaWVudEFkZHJlc3MiOiIxNzYuMTAwLjEwNS4yMzUifQ.dPhjhmREcQJYwsMmsir6vxeNwFsr9wGLNsib5CmUbYA"
private const val JWT_WITH_TRANSFERS_ANY_ROLE =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiLCJ0cmFuc2ZlcnM6YW55Il19fX0.4YTKip8FIZ2VFfPlKFXSw6LMqoOGe2ai8eOH8QXfSxU"
private const val EMPTY_JWT = ""

class CheckoutMultiplexingCredentialsValidatorTest {

    private val jwtValidator = CheckoutMultiplexingCredentialsValidator

    @Test
    fun validateJWT_validJWT_exceptionNotThrown() {
        // Act
        jwtValidator.validateJwt(VALID_JWT)
    }

    @Test(expected = VGSCheckoutInvalidJwtException::class)
    fun validateJWT_emptyJWT_exceptionThrown() {
        // Act
        jwtValidator.validateJwt(EMPTY_JWT)
    }

    @Test(expected = VGSCheckoutJwtRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersWriteRole_exceptionThrown() {
        // Act
        jwtValidator.validateJwt(JWT_WITH_TRANSFERS_WRITE_ROLE)
    }

    @Test(expected = VGSCheckoutJwtRestrictedRoleException::class)
    fun validateJWT_JWTWithTransfersAnyRole_exceptionThrown() {
        // Act
        jwtValidator.validateJwt(JWT_WITH_TRANSFERS_ANY_ROLE)
    }

    @Test
    fun validateJWT_emptyJWT_correctMessage() {
        // Arrange
        val expectedMessage = "Can't parse invalid JWT token."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJwt(EMPTY_JWT)
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
        val expectedMessage = "JWT token contains restricted role [transfers:write]."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJwt(JWT_WITH_TRANSFERS_WRITE_ROLE)
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
        val expectedMessage = "JWT token contains restricted role [transfers:any]."
        var exception: Exception? = null
        // Act
        try {
            jwtValidator.validateJwt(JWT_WITH_TRANSFERS_ANY_ROLE)
        } catch (e: Exception) {
            exception = e
        }
        // Assert
        assertNotNull(exception)
        assertEquals(expectedMessage, exception?.message)
    }
}