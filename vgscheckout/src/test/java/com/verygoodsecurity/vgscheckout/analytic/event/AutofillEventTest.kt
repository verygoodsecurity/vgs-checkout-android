package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

class AutofillEventTest {

    @Test
    fun getData() {
        // Arrange
        val event = AutofillEvent("test_field_name")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("Autofill", data["type"])
        Assert.assertEquals("test_field_name", data["field"])
    }
}