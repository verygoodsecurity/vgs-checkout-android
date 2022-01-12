package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core

import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.*
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
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
        assertEquals(data["type"], TEST_TYPE)
        assertEquals(data["tnt"], ID)
        assertEquals(data["formId"], FORM_ID)
        assertEquals(data["env"], ENVIRONMENT)
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
        assertEquals(data["status"], "Ok")
    }

    @Test
    fun getData_statusOverridden() {
        // Arrange
        val testStatus = "Test_failed"
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = mapOf("status" to testStatus)
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["status"], testStatus)
    }

    @Test
    fun getData_customDataAdded() {
        // Arrange
        val testKey = "test_key"
        val testValue = "test_value"
        val event = object : Event(TEST_TYPE) {

            override val attributes: Map<String, Any> = mapOf(testKey to testValue)
        }
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data[testKey], testValue)
    }

    @Test
    fun getData_autofill_customDataAdded() {
        // Arrange
        val testFieldName = "test_field_name"
        val event = AutofillEvent(testFieldName)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Autofill")
        assertEquals(data["field"], testFieldName)
    }

    @Test
    fun getData_cancel_customDataAdded() {
        // Arrange
        val event = CancelEvent
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Cancel")
    }

    @Test
    fun getData_hostnameValidationSuccess_customDataAdded() {
        // Arrange
        val hostname = "test"
        val event = HostnameValidationEvent(true, hostname)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "HostNameValidation")
        assertEquals(data["hostname"], hostname)
        assertEquals(data["status"], "Ok")
    }

    @Test
    fun getData_hostnameValidationFailed_customDataAdded() {
        // Arrange
        val hostname = "test"
        val event = HostnameValidationEvent(false, hostname)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "HostNameValidation")
        assertEquals(data["hostname"], hostname)
        assertEquals(data["status"], "Failed")
    }

    @Test
    fun getData_initCustom_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Init")
        assertEquals(data["config"], "custom")
    }

    @Test
    fun getData_initPaymentOrchestration_customDataAdded() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.PAYMENT_ORCHESTRATION)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Init")
        assertEquals(data["config"], "payment_orchestration")
    }

    @Test
    fun getData_jwtValidationSuccessful_customDataAdded() {
        // Arrange
        val event = JWTValidationEvent(true)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "JWTValidation")
        assertEquals(data["status"], "Ok")
    }


    @Test
    fun getData_jwtValidationFailed_customDataAdded() {
        // Arrange
        val event = JWTValidationEvent(false)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "JWTValidation")
        assertEquals(data["status"], "Failed")
    }

    @Test
    fun getData_requestSuccessful_customDataAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = true,
            hasCustomHostname = false,
            hasCustomData = false,
            hasCustomHeaders = false,
            hasValidCountries = false,
            mergingPolicy = VGSCheckoutDataMergePolicy.FLAT_JSON,
            invalidFieldTypes = emptyList()
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "BeforeSubmit")
        assertEquals(data["status"], "Ok")
        assertEquals(data["fieldTypes"], null)
        assertEquals((data["content"] as ArrayList<*>).size, 1)
    }

    @Test
    fun getData_requestFailed_customDataAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = false,
            hasCustomHostname = false,
            hasCustomData = false,
            hasCustomHeaders = false,
            hasValidCountries = false,
            mergingPolicy = VGSCheckoutDataMergePolicy.FLAT_JSON,
            invalidFieldTypes = emptyList()
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "BeforeSubmit")
        assertEquals(data["status"], "Failed")
        assertEquals(data["fieldTypes"], null)
        assertEquals((data["content"] as ArrayList<*>).size, 1)
    }

    @Test
    fun getData_requestFailed_contentAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = false,
            hasCustomHostname = true,
            hasCustomData = true,
            hasCustomHeaders = true,
            hasValidCountries = true,
            mergingPolicy = VGSCheckoutDataMergePolicy.FLAT_JSON,
            invalidFieldTypes = emptyList()
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        val content = data["content"] as ArrayList<*>
        // Assert
        assertEquals(data["type"], "BeforeSubmit")
        assertEquals(data["status"], "Failed")
        assertEquals(data["fieldTypes"], null)
        assertTrue(content.contains("custom_hostname"))
        assertTrue(content.contains("custom_data"))
        assertTrue(content.contains("custom_header"))
        assertTrue(content.contains("valid_countries"))
    }

    @Test
    fun getData_responseErrorNull_customDataAdded() {
        // Arrange
        val code = 200
        val latency = 400L
        val event = ResponseEvent(code, latency, null)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Submit")
        assertEquals(data["status"], "Ok")
        assertEquals(data["statusCode"], code)
        assertEquals(data["latency"], latency)
        assertEquals(data["error"], null)
    }

    @Test
    fun getData_responseErrorNotNull_customDataAdded() {
        // Arrange
        val code = 200
        val latency = 400L
        val error = "test"
        val event = ResponseEvent(code, latency, error)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Submit")
        assertEquals(data["status"], "Ok")
        assertEquals(data["statusCode"], code)
        assertEquals(data["latency"], latency)
        assertEquals(data["error"], error)
    }

    @Test
    fun getData_scanScanIdNull_customDataAdded() {
        // Arrange
        val status = "Ok"
        val scannerType = "Test"
        val event = ScanEvent("Ok", scannerType, null)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Scan")
        assertEquals(data["status"], status)
        assertEquals(data["scannerType"], scannerType)
        assertEquals(data["scanId"], null)
    }

    @Test
    fun getData_scanScanIdNotNull_customDataAdded() {
        // Arrange
        val status = "Ok"
        val scannerType = "Test"
        val scanId = "Test"
        val event = ScanEvent("Ok", scannerType, scanId)
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        assertEquals(data["type"], "Scan")
        assertEquals(data["status"], status)
        assertEquals(data["scannerType"], scannerType)
        assertEquals(data["scanId"], scanId)
    }
}