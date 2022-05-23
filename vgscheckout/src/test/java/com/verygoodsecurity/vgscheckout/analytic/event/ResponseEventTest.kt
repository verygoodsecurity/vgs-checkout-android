package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

class ResponseEventTest {

    @Test
    fun getData_responseErrorNull_customDataAdded() {
        // Arrange
        val event = ResponseEvent(200, null, 400L)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Submit", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(400L, data["latency"])
        Assert.assertEquals(null, data["error"])
    }

    @Test
    fun getData_responseErrorNotNull_customDataAdded() {
        // Arrange
        val event = ResponseEvent(200, "test", 400L)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Submit", data["type"])
        Assert.assertEquals(data["status"], "Ok")
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(400L, data["latency"])
        Assert.assertEquals("test", data["error"])
    }
}