package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

class CancelEventTest {

    @Test
    fun getData() {
        // Arrange
        val event = CancelEvent()
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Cancel", data["type"])
    }
}