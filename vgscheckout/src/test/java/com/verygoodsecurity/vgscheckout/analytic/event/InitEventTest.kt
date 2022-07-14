package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import org.junit.Assert
import org.junit.Test

class InitEventTest {

    @Test
    fun getData_custom() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID).build()
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, config)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("custom", data["config"])
        Assert.assertNotNull(data["content"])
    }

    @Test
    fun getData_payout() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID).build()
        val event = InitEvent(InitEvent.ConfigType.PAYOPT, config)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("payopt", data["config"])
        Assert.assertNotNull(data["content"])
    }
}