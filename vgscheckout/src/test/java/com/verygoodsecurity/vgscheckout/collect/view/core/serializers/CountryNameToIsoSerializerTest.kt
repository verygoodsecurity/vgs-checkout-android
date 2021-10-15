package com.verygoodsecurity.vgscheckout.collect.view.core.serializers

import org.junit.Assert.assertEquals
import org.junit.Test

class CountryNameToIsoSerializerTest {

    private val serializer = CountryNameToIsoSerializer()

    @Test
    fun serialize_UnitedStates() {
        // Arrange
        val country = "United States"
        val expected = "US"
        // Act
        val result = serializer.serialize(country)
        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun serialize_Canada() {
        // Arrange
        val country = "Canada"
        val expected = "CA"
        // Act
        val result = serializer.serialize(country)
        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun serialize_Australia() {
        // Arrange
        val country = "Australia"
        val expected = "AU"
        // Act
        val result = serializer.serialize(country)
        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun serialize_NewZealand() {
        // Arrange
        val country = "New Zealand"
        val expected = "NZ"
        // Act
        val result = serializer.serialize(country)
        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun serialize_UnitedKingdom() {
        // Arrange
        val country = "United Kingdom"
        val expected = "GB"
        // Act
        val result = serializer.serialize(country)
        // Assert
        assertEquals(expected, result)
    }
}