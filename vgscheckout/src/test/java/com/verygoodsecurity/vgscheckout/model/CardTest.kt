package com.verygoodsecurity.vgscheckout.model

import org.junit.Assert
import org.junit.Test

class CardTest {

    private val testCardRaw = Card.Raw(true, 200, "", null)

    @Test
    fun twoDigitExpiryYear_fourDigitExpirationYear_readSuccessfully() {
        // Arrange
        val card = Card("", "", "", "", 0, 2022, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("22", card.twoDigitExpiryYear)
    }

    @Test
    fun twoDigitExpiryYear_twoExpirationYear_sameValueReturned() {
        // Arrange
        val card = Card("", "", "", "", 0, 22, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("22", card.twoDigitExpiryYear)
    }
}