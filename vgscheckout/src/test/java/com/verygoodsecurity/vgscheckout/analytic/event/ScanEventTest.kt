package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

class ScanEventTest {

    @Test
    fun getData_scanScanIdNull_customDataAdded() {
        // Arrange
        val event = ScanEvent("Ok", "Test", null)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Scan", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals("Test", data["scannerType"])
        Assert.assertEquals(null, data["scanId"])
    }

    @Test
    fun getData_scanScanIdNotNull_customDataAdded() {
        // Arrange
        val event = ScanEvent("Ok", "Test", "Test")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Scan", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals("Test", data["scannerType"])
        Assert.assertEquals("Test", data["scanId"])
    }
}