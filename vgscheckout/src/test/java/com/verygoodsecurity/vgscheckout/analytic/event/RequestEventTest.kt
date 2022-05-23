package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
import org.junit.Assert
import org.junit.Test

class RequestEventTest {

    @Test
    fun getData_requestSuccessfulCustomConfig_customDataAdded() {
        // Arrange
        val event = RequestEvent(
            isSuccessFull = true,
            invalidFieldTypes = emptyList(),
            VGSCheckoutCustomConfig(ID)
        )
        // Act
        val data = event.getData(ID, FORM_ID, ENVIRONMENT)
        // Assert
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Ok", data["status"])
        Assert.assertEquals(null, data["fieldTypes"])
        Assert.assertEquals(3, (data["content"] as ArrayList<*>).size)
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
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(null, data["fieldTypes"])
        Assert.assertEquals(3, (data["content"] as ArrayList<*>).size)
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
        Assert.assertEquals("BeforeSubmit", data["type"])
        Assert.assertEquals("Failed", data["status"])
        Assert.assertEquals(null, data["fieldTypes"])
        Assert.assertTrue(content.contains("custom_hostname"))
        Assert.assertTrue(content.contains("custom_data"))
        Assert.assertTrue(content.contains("custom_header"))
        Assert.assertTrue(content.contains("valid_countries"))
        Assert.assertTrue(content.contains("on_submit_validation"))
    }
}