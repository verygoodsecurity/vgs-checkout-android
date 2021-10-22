package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VGSCheckoutCustomConfigTest {

    @Test
    fun createCustomConfig_screenshotsDisabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig("")
        // Assert
        assertFalse(config.isScreenshotsAllowed)
    }

    @Test
    fun createCustomConfig_analyticsEnabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig("")
        // Assert
        assertTrue(config.isAnalyticsEnabled)
    }

    @Test
    fun createCustomConfig_envSandboxEnabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig("")
        // Assert
        assert(config.environment is VGSCheckoutEnvironment.Sandbox)
    }
}