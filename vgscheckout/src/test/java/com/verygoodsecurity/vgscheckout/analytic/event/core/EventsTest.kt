package com.verygoodsecurity.vgscheckout.analytic.event.core

import com.verygoodsecurity.vgscheckout.analytic.event.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
import org.junit.Assert.*
import org.junit.Test

private const val ID = "test_vault_id"
private const val FORM_ID = "test_form_id"
private const val ENVIRONMENT = "test_env"

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
    fun getData_parametersAdded() {
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

    @Test
    fun getData_autofill_customDataAdded() {
        // Arrange
        val event = AutofillEvent("test_field_name")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Autofill", data["type"])
        assertEquals("test_field_name", data["field"])
    }

    @Test
    fun getData_cancel_customDataAdded() {
        // Arrange
        val event = CancelEvent
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Cancel", data["type"])
    }

    @Test
    fun getData_hostnameValidationSuccess_customDataAdded() {
        // Arrange
        val event = HostnameValidationEvent(true, "test")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("HostNameValidation", data["type"])
        assertEquals("test", data["hostname"])
        assertEquals("Ok", data["status"])
    }

    @Test
    fun getData_hostnameValidationFailed_customDataAdded() {
        // Arrange
        val event = HostnameValidationEvent(false, "test")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("HostNameValidation", data["type"])
        assertEquals("test", data["hostname"])
        assertEquals("Failed", data["status"])
    }

    @Test
    fun getData_initCustom_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, VGSCheckoutCustomConfig(ID))
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Init", data["type"])
        assertEquals("custom", data["config"])
    }

    @Test
    fun getData_initPaymentOrchestration_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.PAYOPT, VGSCheckoutCustomConfig(ID))
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Init", data["type"])
        assertEquals("payopt", data["config"])
    }

    @Test
    fun getData_jwtValidationSuccessful_customDataAdded() {
        // Arrange
        val event = JWTValidationEvent(true)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("JWTValidation", data["type"])
        assertEquals("Ok", data["status"])
    }


    @Test
    fun getData_jwtValidationFailed_customDataAdded() {
        // Arrange
        val event = JWTValidationEvent(false)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("JWTValidation", data["type"])
        assertEquals("Failed", data["status"])
    }

    @Test
    fun getData_requestSuccessful_customDataAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = true,
            invalidFieldTypes = emptyList(),
            VGSCheckoutCustomConfig(ID)
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("BeforeSubmit", data["type"])
        assertEquals("Ok", data["status"])
        assertEquals(null, data["fieldTypes"])
        assertEquals(3, (data["content"] as ArrayList<*>).size)
    }

    @Test
    fun getData_requestFailed_customDataAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = false,
            invalidFieldTypes = emptyList(),
            VGSCheckoutCustomConfig(ID)
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("BeforeSubmit", data["type"])
        assertEquals("Failed", data["status"])
        assertEquals(null, data["fieldTypes"])
        assertEquals(3, (data["content"] as ArrayList<*>).size)
    }

    @Test
    fun getData_requestFailed_contentAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = false,
            invalidFieldTypes = emptyList(),
            VGSCheckoutCustomConfig(
                ID,
                routeConfig = VGSCheckoutCustomRouteConfig(
                    hostnamePolicy = VGSCheckoutHostnamePolicy.CustomHostname(""),
                    requestOptions = VGSCheckoutCustomRequestOptions(
                        extraHeaders = mapOf("" to ""),
                        extraData = mapOf("" to "")
                    )
                ),
                formConfig = VGSCheckoutCustomFormConfig(
                    addressOptions = VGSCheckoutCustomBillingAddressOptions(
                        countryOptions = VGSCheckoutCustomCountryOptions(
                            validCountries = listOf("")
                        )
                    )
                )
            )
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        val content = data["content"] as ArrayList<*>
        // Assert
        assertEquals("BeforeSubmit", data["type"])
        assertEquals("Failed", data["status"])
        assertEquals(null, data["fieldTypes"])
        assertTrue(content.contains("custom_hostname"))
        assertTrue(content.contains("custom_data"))
        assertTrue(content.contains("custom_header"))
        assertTrue(content.contains("valid_countries"))
        assertTrue(content.contains("on_submit_validation"))
    }

    @Test
    fun getData_responseErrorNull_customDataAdded() {
        // Arrange
        val event = ResponseEvent(200, null, 400L)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Submit", data["type"])
        assertEquals("Ok", data["status"])
        assertEquals(200, data["statusCode"])
        assertEquals(400L, data["latency"])
        assertEquals(null, data["error"])
    }

    @Test
    fun getData_responseErrorNotNull_customDataAdded() {
        // Arrange
        val event = ResponseEvent(200, "test", 400L)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Submit", data["type"])
        assertEquals(data["status"], "Ok")
        assertEquals(200, data["statusCode"])
        assertEquals(400L, data["latency"])
        assertEquals("test", data["error"])
    }

    @Test
    fun getData_scanScanIdNull_customDataAdded() {
        // Arrange
        val event = ScanEvent("Ok", "Test", null)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Scan", data["type"])
        assertEquals("Ok", data["status"])
        assertEquals("Test", data["scannerType"])
        assertEquals(null, data["scanId"])
    }

    @Test
    fun getData_scanScanIdNotNull_customDataAdded() {
        // Arrange
        val event = ScanEvent("Ok", "Test", "Test")
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals("Scan", data["type"])
        assertEquals("Ok", data["status"])
        assertEquals("Test", data["scannerType"])
        assertEquals("Test", data["scanId"])
    }

    @Test
    fun getData_finInstrumentLoad_customDataAdded() {
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
        assertEquals("FinInstrument", data["type"])
        assertEquals("LoadFinInstruments", data["method"])
        assertEquals(200, data["statusCode"])
        assertEquals("Error message", data["error"])
        assertEquals("payopt", data["config"])
        assertEquals(10, data["totalCount"])
        assertEquals(5, data["failedCount"])
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
        assertEquals("FinInstrument", data["type"])
        assertEquals("DeleteFinInstrument", data["method"])
        assertEquals(200, data["statusCode"])
        assertEquals("Error message", data["error"])
        assertEquals("payopt", data["config"])
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
        assertEquals("FinInstrument", data["type"])
        assertEquals("CreateFinInstrument", data["method"])
        assertEquals(200, data["statusCode"])
        assertEquals("Error message", data["error"])
        assertEquals("payopt", data["config"])
    }

    @Test
    fun getData_addPaymentMethod_customDataAdded() {
        // Arrange
        val event = PaymentMethodSelectedEvent(
            isPreSavedCard = true,
            isCustomConfig = true,
        )
        //Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        //Assert
        assertEquals("AddCardPaymentMethod", data["type"])
        assertEquals("savedCard", data["paymentMethod"])
        assertEquals("custom", data["config"])
        assertEquals("addCard", data["configType"])
    }
}