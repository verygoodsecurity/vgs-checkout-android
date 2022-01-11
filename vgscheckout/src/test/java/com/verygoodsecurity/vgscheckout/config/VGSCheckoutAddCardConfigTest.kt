package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTParseException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutJWTRestrictedRoleException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import org.junit.Assert.*
import org.junit.Test

class VGSCheckoutAddCardConfigTest {

    @Test
    fun createAddCardConfig_validJWT_objectCreated() {
        // Act
        val config = VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS, "")
        // Assert
        assertNotNull(config)
    }

    @Test
    fun createAddCardConfig_envSandboxEnabledByDefault() {
        // Act
        val config = VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS, "")
        // Assert
        assert(config.environment is VGSCheckoutEnvironment.Sandbox)
    }

    @Test
    fun createAddCardConfig_screenshotsDisabledByDefault() {
        // Act
        val config = VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS, "")
        // Assert
        assertFalse(config.isScreenshotsAllowed)
    }

    @Test
    fun createAddCardConfig_analyticsEnabledByDefault() {
        // Act
        val config = VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS, "")
        // Assert
        assertTrue(config.isAnalyticsEnabled)
    }

    @Test(expected = VGSCheckoutJWTParseException::class)
    fun createAddCardConfig_emptyJWT_exceptionThrown() {
        // Act
        VGSCheckoutAddCardConfig("", "")
    }

    @Test(expected = VGSCheckoutJWTRestrictedRoleException::class)
    fun createAddCardConfig_invalidJWT_exceptionThrown() {
        // Act
        VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_WRITE, "")
    }
}