package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import org.junit.Assert.assertFalse
import org.junit.Test

class VGSCheckoutCustomConfigTest {

    @Test
    fun createCustomConfig_screenshotsDisabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder("")
            .build()
        // Assert
        assertFalse(config.isScreenshotsAllowed)
    }

    @Test
    fun createCustomConfig_envSandboxEnabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder("")
            .build()
        // Assert
        assert(config.environment is VGSCheckoutEnvironment.Sandbox)
    }
}