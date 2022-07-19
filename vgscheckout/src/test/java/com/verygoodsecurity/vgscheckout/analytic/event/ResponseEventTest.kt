package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID_KEY
import org.junit.Assert
import org.junit.Test

class ResponseEventTest {

    @Test
    fun getData_errorMessageNull() {
        // Arrange
        val event = ResponseEvent(200, null, 400L)
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Submit", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(400L, data["latency"])
        Assert.assertEquals(null, data["error"])
    }

    @Test
    fun getData__errorMessageNotNull() {
        // Arrange
        val event = ResponseEvent(200, "test", 400L)
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Submit", data["type"])
        Assert.assertEquals(data["status"], "Ok")
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(400L, data["latency"])
        Assert.assertEquals("test", data["error"])
    }
}