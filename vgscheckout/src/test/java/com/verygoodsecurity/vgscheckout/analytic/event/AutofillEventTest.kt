package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID_KEY
import org.junit.Assert
import org.junit.Test

class AutofillEventTest {

    @Test
    fun getData() {
        // Arrange
        val event = AutofillEvent("test_field_name")
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        Assert.assertEquals("Autofill", data["type"])
        Assert.assertEquals("test_field_name", data["field"])
    }
}