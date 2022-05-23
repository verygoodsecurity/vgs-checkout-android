package com.verygoodsecurity.vgscheckout.analytic.event.core

import org.junit.Assert.*
import org.junit.Test

const val ID = "test_vault_id"
const val FORM_ID = "test_form_id"
const val ENVIRONMENT = "test_env"

private const val TEST_TYPE = "Test"

class EventsTest {

    @Test
    fun getData_defaultDataAdded() {
        // Arrange
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(TEST_TYPE, data["type"])
        assertEquals(ID, data["tnt"])
        assertEquals(FORM_ID, data["formId"])
        assertEquals(ENVIRONMENT, data["env"])
        assertNotNull(data["status"])
        assertNotNull(data["type"])
        assertNotNull(data["localTimestamp"])
        assertNotNull(data["clientTimestamp"])
        assertNotNull(data["source"])
        assertNotNull(data["version"])
        assertNotNull(data["ua"])
        assertNotNull((data["ua"] as Map<*, *>)["source"])
        assertNotNull((data["ua"] as Map<*, *>)["osVersion"])
        assertTrue((data["ua"] as Map<*, *>).containsKey("device"))
        assertTrue((data["ua"] as Map<*, *>).containsKey("deviceModel"))
    }

    @Test
    fun getData_statusOkByDefault() {
        // Arrange
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Ok", data["status"])
    }

    @Test
    fun getData_statusOverridden() {
        // Arrange
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = mapOf("status" to "Test_failed")
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Test_failed", data["status"])
    }

    @Test
    fun getData_customDataAdded() {
        // Arrange
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = mapOf("test_key" to "test_value")
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("test_value", data["test_key"])
    }
}