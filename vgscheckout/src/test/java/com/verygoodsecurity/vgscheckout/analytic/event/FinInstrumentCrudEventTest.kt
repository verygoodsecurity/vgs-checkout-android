package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.analytic.event.core.ROUTE_ID_KEY
import org.junit.Assert
import org.junit.Test

class FinInstrumentCrudEventTest {

    @Test
    fun getData_finInstrumentLoad_successfulCustomConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.load(
            200,
            true,
            null,
            true,
            10,
            5
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("LoadFinInstruments", data["method"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(null, data["error"])
        Assert.assertEquals("custom", data["config"])
        Assert.assertEquals(10, data["totalCount"])
        Assert.assertEquals(5, data["failedCount"])
    }

    @Test
    fun getData_finInstrumentLoad_unsuccessfulPaymentConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.load(
            500,
            false,
            "Error message",
            false,
            10,
            5
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("LoadFinInstruments", data["method"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(500, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
        Assert.assertEquals(10, data["totalCount"])
        Assert.assertEquals(5, data["failedCount"])
    }

    @Test
    fun getData_finInstrumentDelete_successfulCustomConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.delete(
            200,
            true,
            null,
            true
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("DeleteFinInstrument", data["method"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(null, data["error"])
        Assert.assertEquals("custom", data["config"])
    }

    @Test
    fun getData_finInstrumentDelete_unsuccessfulPaymentConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.delete(
            500,
            false,
            "Error message",
            false
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("DeleteFinInstrument", data["method"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(500, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
    }

    @Test
    fun getData_finInstrumentCreate_successfulCustomConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.create(
            200,
            true,
            null,
            true
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("CreateFinInstrument", data["method"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(200, data["statusCode"])
        Assert.assertEquals(null, data["error"])
        Assert.assertEquals("custom", data["config"])
    }

    @Test
    fun getData_finInstrumentCreate_unsuccessfulPaymentConfig() {
        // Arrange
        val event = FinInstrumentCrudEvent.create(
            500,
            false,
            "Error message",
            false
        )
        //Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        //Assert
        Assert.assertEquals("FinInstrument", data["type"])
        Assert.assertEquals("CreateFinInstrument", data["method"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(500, data["statusCode"])
        Assert.assertEquals("Error message", data["error"])
        Assert.assertEquals("payopt", data["config"])
    }
}