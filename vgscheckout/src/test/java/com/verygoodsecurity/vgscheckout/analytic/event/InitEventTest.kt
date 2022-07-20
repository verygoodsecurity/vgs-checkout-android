package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID_KEY
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import org.junit.Assert
import org.junit.Test

class InitEventTest {

    @Test
    fun getData_custom() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY).build()
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, config)
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("custom", data["config"])
        Assert.assertNotNull(data["content"])
    }

    @Test
    fun getData_payout() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY).build()
        val event = InitEvent(InitEvent.ConfigType.PAYOPT, config)
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Init", data["type"])
        Assert.assertEquals("payopt", data["config"])
        Assert.assertNotNull(data["content"])
    }
}