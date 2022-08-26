package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.analytic.event.core.ID_KEY
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import org.junit.Assert.*
import org.junit.Test

class VGSCheckoutCustomConfigTest {

    @Test
    fun createDefaultCustomConfig() {
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .build()

        assertEquals(ID_KEY, config.id)
        assertEquals("", config.routeId)
        assertEquals(VGSCheckoutEnvironment.Sandbox(), config.environment)
        assertEquals(false, config.isScreenshotsAllowed)

        assertEquals(
            null,
            config.formConfig.cardOptions.expirationDateOptions.dateSeparateSerializer
        )
        assertEquals("", config.formConfig.cardOptions.expirationDateOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.cardOptions.expirationDateOptions.visibility
        )
        assertEquals(
            EXPIRY_DATE_FORMAT,
            config.formConfig.cardOptions.expirationDateOptions.inputFormatRegex
        )
        assertEquals(
            EXPIRY_DATE_FORMAT,
            config.formConfig.cardOptions.expirationDateOptions.outputFormatRegex
        )

        assertEquals("", config.formConfig.cardOptions.cardHolderOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.cardOptions.cardHolderOptions.visibility
        )

        assertEquals("", config.formConfig.cardOptions.cvcOptions.fieldName)
        assertEquals(false, config.formConfig.cardOptions.cvcOptions.isIconHidden)

        assertEquals("", config.formConfig.cardOptions.cardNumberOptions.fieldName)
        assertEquals(false, config.formConfig.cardOptions.cardNumberOptions.isIconHidden)

        assertEquals("", config.formConfig.addressOptions.countryOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.countryOptions.visibility
        )
        assertTrue(config.formConfig.addressOptions.countryOptions.validCountries.isEmpty())

        assertEquals("", config.formConfig.addressOptions.cityOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.cityOptions.visibility
        )

        assertEquals("", config.formConfig.addressOptions.addressOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.addressOptions.visibility
        )

        assertEquals("", config.formConfig.addressOptions.optionalAddressOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.optionalAddressOptions.visibility
        )

        assertEquals("", config.formConfig.addressOptions.postalCodeOptions.fieldName)
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.postalCodeOptions.visibility
        )

        assertEquals(
            VGSCheckoutBillingAddressVisibility.HIDDEN,
            config.formConfig.addressOptions.visibility
        )
        assertEquals(
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT,
            config.formConfig.validationBehaviour
        )

        assertEquals(
            "",
            config.routeConfig.path
        )
        assertEquals(
            VGSCheckoutHostnamePolicy.Vault,
            config.routeConfig.hostnamePolicy
        )
        assertEquals(
            VGSCheckoutHttpMethod.POST,
            config.routeConfig.requestOptions.httpMethod
        )
        assertTrue(config.routeConfig.requestOptions.extraHeaders.isEmpty())
        assertTrue(config.routeConfig.requestOptions.extraData.isEmpty())
        assertEquals(
            VGSCheckoutDataMergePolicy.FLAT_JSON,
            config.routeConfig.requestOptions.mergePolicy
        )
    }

    @Test
    fun setRouteId() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setRouteId(ID_KEY)
            .build()
        // Assert
        assertEquals(ID_KEY, config.routeId)
    }

    @Test
    fun setEnvironment() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setEnvironment(VGSCheckoutEnvironment.Live())
            .build()
        // Assert
        assertEquals(VGSCheckoutEnvironment.Live(), config.environment)
    }

    @Test
    fun setIsScreenshotsAllowed() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setIsScreenshotsAllowed(true)
            .build()
        // Assert
        assertEquals(true, config.isScreenshotsAllowed)
    }

    @Test
    fun setValidationBehaviour() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setFormValidationBehaviour(VGSCheckoutFormValidationBehaviour.ON_FOCUS)
            .build()
        // Assert
        assertEquals(VGSCheckoutFormValidationBehaviour.ON_FOCUS, config.formConfig.validationBehaviour)
    }

    @Test
    fun configureBillingAddressSettings() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setAddressOptions(FIELD_NAME, VGSCheckoutFieldVisibility.HIDDEN)
            .setOptionalAddressOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setPostalCodeOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setOptionalAddressOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setAddressOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setCountryOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setCityOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.HIDDEN)
            .build()

        // Assert
        assertEquals(VGSCheckoutBillingAddressVisibility.HIDDEN, config.formConfig.addressOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.addressOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.addressOptions.addressOptions.fieldName)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.optionalAddressOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.addressOptions.optionalAddressOptions.fieldName)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.postalCodeOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.addressOptions.postalCodeOptions.fieldName)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.countryOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.addressOptions.countryOptions.fieldName)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.cityOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.addressOptions.cityOptions.fieldName)
    }

    @Test
    fun configureCardFormSettings() {
        // Act
        val serializer = VGSDateSeparateSerializer(
            "month",
            "year"
        )
        val config = VGSCheckoutCustomConfig.Builder(ID_KEY)
            .setCardNumberOptions(FIELD_NAME, false)
            .setCardHolderOptions(FIELD_NAME,VGSCheckoutFieldVisibility.HIDDEN)
            .setCVCOptions(FIELD_NAME, false)
            .setExpirationDateOptions(
                FIELD_NAME,
                serializer,
                FULL_EXPIRY_DATE_FORMAT,
                FULL_EXPIRY_DATE_FORMAT)
            .build()

        // Assert
        assertEquals(FIELD_NAME, config.formConfig.cardOptions.expirationDateOptions.fieldName)
        assertEquals(FULL_EXPIRY_DATE_FORMAT, config.formConfig.cardOptions.expirationDateOptions.inputFormatRegex)
        assertEquals(FULL_EXPIRY_DATE_FORMAT, config.formConfig.cardOptions.expirationDateOptions.outputFormatRegex)
        assertEquals(serializer, config.formConfig.cardOptions.expirationDateOptions.dateSeparateSerializer)
        assertEquals(FIELD_NAME, config.formConfig.cardOptions.cardHolderOptions.fieldName)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.cardOptions.cardHolderOptions.visibility)
        assertEquals(FIELD_NAME, config.formConfig.cardOptions.cardNumberOptions.fieldName)
        assertEquals(false, config.formConfig.cardOptions.cardNumberOptions.isIconHidden)
        assertEquals(FIELD_NAME, config.formConfig.cardOptions.cvcOptions.fieldName)
        assertEquals(false, config.formConfig.cardOptions.cvcOptions.isIconHidden)
    }

    @Test
    fun createCustomConfig_screenshotsDisabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder("")
            .build()
        // Assert
        assertFalse(config.isScreenshotsAllowed)
    }

    @Test
    fun createCustomConfig_envSandboxEnabledByDefault() {
        // Act
        val config = VGSCheckoutCustomConfig.Builder("")
            .build()
        // Assert
        assert(config.environment is VGSCheckoutEnvironment.Sandbox)
    }

    companion object {
        private const val EXPIRY_DATE_FORMAT = "MM/yy"
        private const val FULL_EXPIRY_DATE_FORMAT = "MM/yyyy"
        private const val FIELD_NAME = "test_name"
    }
}