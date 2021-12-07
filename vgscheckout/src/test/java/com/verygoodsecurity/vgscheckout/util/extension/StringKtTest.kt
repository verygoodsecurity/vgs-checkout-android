package com.verygoodsecurity.vgscheckout.util.extension

import org.junit.Assert.*
import org.junit.Test

private const val VALID_JWT_STRUCTURE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6IkpXVCB0b2tlbiJ9.hhCQqSlTxzqbIxELJQu43b_ooQ2K8LPwzTuOGu1yBxU"
private const val INVALID_JWT_STRUCTURE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJ0b2tlbiI6IkpXVCB0b2tlbiJ9hhCQqSlTxzqbIxELJQu43b_ooQ2K8LPwzTuOGu1yBxU"

class StringKtTest {

    @Test
    fun decodeJWTPayload_validJWT_decodedPayloadReturned() {
        // Arrange
        val expected = "{\"token\":\"JWT token\"}"
        // Act
        val decodeResult = VALID_JWT_STRUCTURE.decodeJWTPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun decodeJWTPayload_invalidJWT_nullReturned() {
        // Arrange
        val expected: String? = null
        // Act
        val decodeResult = INVALID_JWT_STRUCTURE.decodeJWTPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun decodeJWTPayload_emptyJWT_nullReturned() {
        // Arrange
        val expected: String? = null
        // Act
        val decodeResult = "".decodeJWTPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun toJson_validString_jsonReturned() {
        // Arrange
        val data = "{\"test\":\"test\"}"
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNotNull(jsonResult)
        assertTrue(jsonResult!!.has("test"))
        assertEquals("test", jsonResult.get("test"))
    }

    @Test
    fun toJson_emptyString_nullReturned() {
        // Arrange
        val data = ""
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNull(jsonResult)
    }

    @Test
    fun toJson_invalidString_nullReturned() {
        // Arrange
        val data = "\"test\":\"test\"}"
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNull(jsonResult)
    }
}