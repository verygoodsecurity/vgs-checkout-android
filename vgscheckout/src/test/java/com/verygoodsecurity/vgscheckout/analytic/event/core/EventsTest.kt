package com.verygoodsecurity.vgscheckout.analytic.event.core

import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import org.junit.Assert.*
import org.junit.Test

const val ID_KEY = "test_vault_id"
const val FORM_ID_KEY = "test_form_id"
const val ROUTE_ID_KEY = "test_route_id"
const val ENVIRONMENT_KEY = "test_env"

private const val TEST_TYPE = "Test"

class EventsTest {

    @Test
    fun getData_defaultDataAdded() {
        // Arrange
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        assertEquals(TEST_TYPE, data["type"])
        assertEquals(ID_KEY, data["tnt"])
        assertEquals(FORM_ID_KEY, data["formId"])
        assertEquals(ROUTE_ID_KEY, data["routeId"])
        assertEquals(ENVIRONMENT_KEY, data["env"])
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
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
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
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
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
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        // Assert
        assertEquals("test_value", data["test_key"])
    }

    @Test
    fun getData_customHostnameAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setHostnamePolicy(VGSCheckoutHostnamePolicy.CustomHostname(""))
            .build()
        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("custom_hostname"))
    }

    @Test
    fun getData_customDataAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setHostnamePolicy(VGSCheckoutHostnamePolicy.CustomHostname(""))
            .setPayload(mapOf("test" to "test"))
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("custom_data"))
    }

    @Test
    fun getData_customHeaderAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setHostnamePolicy(VGSCheckoutHostnamePolicy.CustomHostname(""))
            .setHeaders(mapOf("test" to "test"))
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("custom_header"))
    }

    @Test
    fun getData_validCountriesAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setCountryOptions(fieldName = "", validCountries = listOf("Test"))
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("valid_countries"))
    }

    @Test
    fun getData_onSubmitValidationAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setFormValidationBehaviour(VGSCheckoutFormValidationBehaviour.ON_SUBMIT)
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("on_submit_validation"))
    }

    @Test
    fun getData_onFocusValidationAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setFormValidationBehaviour(VGSCheckoutFormValidationBehaviour.ON_FOCUS)
            .build()
        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("on_focus_validation"))
    }

    @Test
    fun getData_billingAddressVisibleAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.VISIBLE)
            .build()
        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("billing_address_visible"))
    }

    @Test
    fun getData_billingAddressHiddenAddedToContent() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.HIDDEN)
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("billing_address_hidden"))
    }

    @Test
    fun getData_saveCardCheckboxToContent() {
        // Arrange
        val config = VGSCheckoutAddCardConfig.Builder(ID_KEY)
            .setAccessToken("")
            .setIsSaveCardOptionVisible(true)
            .build()

        val event = object : Event(TEST_TYPE, config) {

            override val attributes: Map<String, Any> = emptyMap()
        }
        // Act
        val data = event.getData(ID_KEY, ENVIRONMENT_KEY, FORM_ID_KEY, ROUTE_ID_KEY)
        val content = data["content"] as List<*>
        // Assert
        assertTrue(content.contains("save_card_checkbox"))
    }
}