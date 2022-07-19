package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID
import org.junit.Assert
import org.junit.Test

class JWTValidationEventTest {

    @Test
    fun getData_successful() {
        // Arrange
        val event = JWTValidationEvent(true)
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("JWTValidation", data["type"])
        Assert.assertEquals("Ok", data["status"])
    }


    @Test
    fun getData_failed() {
        // Arrange
        val event = JWTValidationEvent(false)
        // Act
        val data = event.getData(ID, ENVIRONMENT, FORM_ID, ROUTE_ID)
        // Assert
        Assert.assertEquals("JWTValidation", data["type"])
        Assert.assertEquals("Failed", data["status"])
    }
}