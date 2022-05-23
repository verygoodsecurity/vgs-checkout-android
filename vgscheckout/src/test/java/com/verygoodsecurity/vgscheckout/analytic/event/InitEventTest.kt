package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import org.junit.Assert
import org.junit.Test

class InitEventTest {

    @Test
    fun getData_initCustom_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, VGSCheckoutCustomConfig(ID))
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("custom", data["config"])
    }

    @Test
    fun getData_initPaymentOrchestration_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.PAYOPT, VGSCheckoutCustomConfig(ID))
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("payopt", data["config"])
    }
}