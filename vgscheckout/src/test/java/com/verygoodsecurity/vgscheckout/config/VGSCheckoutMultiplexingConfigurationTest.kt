package com.verygoodsecurity.vgscheckout.config

import org.junit.Assert.*
import org.junit.Test

private const val VALID_JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50eHh4eHh4eCI6eyJyb2xlcyI6WyJmaW5hbmNpYWwtaW5zdHJ1bWVudHM6d3JpdGUiXX19fQ.-zjySsFy3G3cy-wkw1VpHRUS10OmKlI_rElg_4_3lsw"

class VGSCheckoutMultiplexingConfigurationTest {

    @Test
    fun createMultiplexingConfig_validJWT_objectCreated() {
        // Act
        val config = VGSCheckoutMultiplexingConfiguration(VALID_JWT, "")
        // Assert
        assertNotNull(config)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createMultiplexingConfig_emptyJWT_exceptionThrown() {
        // Act
        VGSCheckoutMultiplexingConfiguration("", "")
    }
}