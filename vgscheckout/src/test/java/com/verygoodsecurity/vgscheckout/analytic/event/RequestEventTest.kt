package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import org.junit.Assert
import org.junit.Test

class RequestEventTest {

    @Test
    fun getData_successful() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID).build()
        val event = RequestEvent(
            isSuccessFull = true,
            invalidFieldTypes = emptyList(),
            config
        )
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(null, data["fieldTypes"])
        Assert.assertNotNull(data["content"])
    }

    @Test
    fun getData_failed() {
        // Arrange
        val expectedInvalidField = listOf("test")
        val config = VGSCheckoutCustomConfig.Builder(ID).build()
        val event = RequestEvent(
            isSuccessFull = false,
            invalidFieldTypes = expectedInvalidField,
            config
        )
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(expectedInvalidField, data["fieldTypes"])
        Assert.assertNotNull(data["content"])
    }
}