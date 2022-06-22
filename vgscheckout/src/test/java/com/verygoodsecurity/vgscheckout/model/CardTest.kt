package com.verygoodsecurity.vgscheckout.model

import org.junit.Assert
import org.junit.Test

class CardTest {

    private val testCardRaw = Card.Raw(true, 200, "", null)

    @Test
    fun lastFour_validCardNumber_readSuccessfully() {
        // Arrange
        val card = Card("", "", "4111111111111111", 0, 0, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("1111", card.lastFour)
    }

    @Test
    fun lastFour_shortCardNumber_sameValueReturned() {
        // Arrange
        val card = Card("", "", "411", 0, 0, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("411", card.lastFour)
    }

    @Test
    fun lastFour_emptyCardNumber_emptyStringReturned() {
        // Arrange
        val card = Card("", "", "", 0, 0, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("", card.lastFour)
    }

    @Test
    fun twoDigitExpiryYear_fourDigitExpirationYear_readSuccessfully() {
        // Arrange
        val card = Card("", "", "", 0, 2022, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("22", card.twoDigitExpiryYear)
    }

    @Test
    fun twoDigitExpiryYear_twoExpirationYear_sameValueReturned() {
        // Arrange
        val card = Card("", "", "", 0, 22, "VISA", testCardRaw)
        // Assert
        Assert.assertEquals("22", card.twoDigitExpiryYear)
    }
}