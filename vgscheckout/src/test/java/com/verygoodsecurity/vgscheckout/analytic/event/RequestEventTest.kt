package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import org.junit.Assert
import org.junit.Test

class RequestEventTest {

    @Test
    fun getData_successful() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = true,
            invalidFieldTypes = emptyList(),
            VGSCheckoutCustomConfig(ID)
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
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
        val event = RequestEvent(
            isSuccessFull = false,
            invalidFieldTypes = expectedInvalidField,
            VGSCheckoutCustomConfig(ID)
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(expectedInvalidField, data["fieldTypes"])
        Assert.assertNotNull(data["content"])
    }
}