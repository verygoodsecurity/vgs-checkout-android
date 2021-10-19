package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutInvalidJwtException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutJwtRestrictedRoleException
import org.junit.Assert.*
import org.junit.Test

private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiXX19fQ.-zjySsFy3G3cy-wkw1VpHRUS10OmKlI_rElg_4_3lsw"
private const val INVALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA1MjIyODcsImlhdCI6MTYzMDUyMTk4NywianRpIjoiNWVjZGVyMzItOWM2My00YzAyLWE1MjMtYWE4MjRjYzI3NDRjIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLnZlcnlnb29kc2VjdXJpdHkuY29tL2F1dGgvcmVhbG1zL3ZncyIsImF1ZCI6Im11bHRpcGxleGluZy1hcHAtdG50c2htbGpsYTciLCJzdWIiOiI3ZmZmZDFkZS1hM2UyLTRkMDAtYjAzZi00Mjg4MjJhMmFlOWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwic2Vzc2lvbl9zdGF0ZSI6IjQzMTJkODcyLTNmZWUtNDlhMy05OGNmLTc1YjAwMDA5N2U0NiIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSJdfSwibXVsdGlwbGV4aW5nLWFwcC10bnR5eXl5eXkiOnsicm9sZXMiOlsiZmluYW5jaWFsLWluc3RydW1lbnRzOndyaXRlIiwidHJhbnNmZXJzOndyaXRlIl19fSwic2NvcGUiOiJ1c2VyX2lkIHNlcnZpY2UtYWNjb3VudCIsInNlcnZpY2VfYWNjb3VudCI6dHJ1ZSwiY2xpZW50SWQiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwiY2xpZW50SG9zdCI6IjE3Ni4xMDAuMTA1LjIzNSIsImNsaWVudEFkZHJlc3MiOiIxNzYuMTAwLjEwNS4yMzUifQ.dPhjhmREcQJYwsMmsir6vxeNwFsr9wGLNsib5CmUbYA"

class VGSCheckoutMultiplexingConfigTest {

    @Test
    fun createMultiplexingConfig_validJWT_objectCreated() {
        // Act
        val config = VGSCheckoutMultiplexingConfig(VALID_JWT, "")
        // Assert
        assertNotNull(config)
    }

    @Test(expected = VGSCheckoutInvalidJwtException::class)
    fun createMultiplexingConfig_emptyJWT_exceptionThrown() {
        // Act
        VGSCheckoutMultiplexingConfig("", "")
    }

    @Test(expected = VGSCheckoutJwtRestrictedRoleException::class)
    fun createMultiplexingConfig_invalidJWT_exceptionThrown() {
        // Act
        VGSCheckoutMultiplexingConfig(INVALID_JWT, "")
    }
}