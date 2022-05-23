package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

// TODO: Improve
class FinInstrumentCrudEventTest {

    @Test
    fun getData_finInstrumentLoad_successfulCustomConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.load(
            200,
            true,
            "Error message",
            false,
            10,
            5
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("LoadFinInstruments", data["method"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
        Assert.assertEquals(10, data["totalCount"])
        Assert.assertEquals(5, data["failedCount"])
    }

    @Test
    fun getData_finInstrumentDelete_customDataAdded() {
        // Arrange
        val event = FinInstrumentCrudEvent.delete(
            200,
            true,
            "Error message",
            false
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("DeleteFinInstrument", data["method"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
    }

    @Test
    fun getData_finInstrumentCreate_customDataAdded() {
        // Arrange
        val event = FinInstrumentCrudEvent.create(
            200,
            true,
            "Error message",
            false
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("CreateFinInstrument", data["method"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
    }
}