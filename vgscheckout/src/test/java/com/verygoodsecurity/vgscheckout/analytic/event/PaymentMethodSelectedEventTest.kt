package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import org.junit.Assert
import org.junit.Test

class PaymentMethodSelectedEventTest {

    @Test
    fun getData_savedCardWithCustomConfig() {
        // Arrange
        val event = PaymentMethodSelectedEvent(
            isPreSavedCard = true,
            isCustomConfig = true,
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        Assert.assertEquals("PaymentMethodSelected", data["type"])
        Assert.assertEquals("savedCard", data["paymentMethod"])
        Assert.assertEquals("custom", data["config"])
        Assert.assertEquals("addCard", data["configType"])
    }

    @Test
    fun getData_newCardWithAddCardConfig() {
        // Arrange
        val event = PaymentMethodSelectedEvent(
            isPreSavedCard = false,
            isCustomConfig = false,
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        Assert.assertEquals("PaymentMethodSelected", data["type"])
        Assert.assertEquals("newCard", data["paymentMethod"])
        Assert.assertEquals("payopt", data["config"])
        Assert.assertEquals("addCard", data["configType"])
    }
}