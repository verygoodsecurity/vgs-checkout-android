package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID
import org.junit.Assert
import org.junit.Test

class HostnameValidationEventTest {

    @Test
    fun getData_successful() {
        // Arrange
        val event = HostnameValidationEvent(true, "test")
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("HostNameValidation", data["type"])
        Assert.assertEquals("test", data["hostname"])
        Assert.assertEquals("Ok", data["status"])
    }

    @Test
    fun getData_failed() {
        // Arrange
        val event = HostnameValidationEvent(false, "test")
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("HostNameValidation", data["type"])
        Assert.assertEquals("test", data["hostname"])
        Assert.assertEquals("Failed", data["status"])
    }
}