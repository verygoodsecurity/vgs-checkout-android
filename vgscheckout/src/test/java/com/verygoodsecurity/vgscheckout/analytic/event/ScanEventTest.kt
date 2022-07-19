package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID_KEY
import org.junit.Assert
import org.junit.Test

class ScanEventTest {

    @Test
    fun getData_scanIdNull() {
        // Arrange
        val event = ScanEvent("Ok", "Test", null)
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Scan", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals("Test", data["scannerType"])
        Assert.assertEquals(null, data["scanId"])
    }

    @Test
    fun getData_scanIdNotNull() {
        // Arrange
        val event = ScanEvent("Ok", "Test", "Test")
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Scan", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals("Test", data["scannerType"])
        Assert.assertEquals("Test", data["scanId"])
    }
}