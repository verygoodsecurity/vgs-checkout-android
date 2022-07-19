package com.verygoodsecurity.vgscheckout.config

import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class VGSCheckoutPaymentConfigTest {

    @Test
    fun createDefaultCustomConfig() {
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .build()

        assertEquals(ID, config.id)
        assertEquals(ROUTE_ID, config.routeId)
        assertEquals(VGSCheckoutEnvironment.Sandbox(), config.environment)
        assertEquals(false, config.isScreenshotsAllowed)

        assertEquals(
            VGSDateSeparateSerializer(
                MONTH_FIELD_NAME,
                YEAR_FIELD_NAME
            ),
            config.formConfig.cardOptions.expirationDateOptions.dateSeparateSerializer
        )
        assertEquals(
            EXP_FIELD_NAME,
            config.formConfig.cardOptions.expirationDateOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.cardOptions.expirationDateOptions.visibility
        )
        assertEquals(
            EXPIRY_DATE_INPUT_FORMAT,
            config.formConfig.cardOptions.expirationDateOptions.inputFormatRegex
        )
        assertEquals(
            EXPIRY_DATE_OUTPUT_FORMAT,
            config.formConfig.cardOptions.expirationDateOptions.outputFormatRegex
        )

        assertEquals(
            CARD_HOLDER_FIELD_NAME,
            config.formConfig.cardOptions.cardHolderOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.cardOptions.cardHolderOptions.visibility
        )

        assertEquals(
            CVC_FIELD_NAME,
            config.formConfig.cardOptions.cvcOptions.fieldName
        )
        assertEquals(false, config.formConfig.cardOptions.cvcOptions.isIconHidden)

        assertEquals(
            CARD_NUMBER_FIELD_NAME,
            config.formConfig.cardOptions.cardNumberOptions.fieldName
        )
        assertEquals(false, config.formConfig.cardOptions.cardNumberOptions.isIconHidden)

        assertEquals(
            COUNTRY_FIELD_NAME,
            config.formConfig.addressOptions.countryOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.countryOptions.visibility
        )
        Assert.assertTrue(config.formConfig.addressOptions.countryOptions.validCountries.isEmpty())

        assertEquals(
            CITY_FIELD_NAME,
            config.formConfig.addressOptions.cityOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.cityOptions.visibility
        )

        assertEquals(
            ADDRESS1_FIELD_NAME,
            config.formConfig.addressOptions.addressOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.addressOptions.visibility
        )

        assertEquals(
            ADDRESS2_FIELD_NAME,
            config.formConfig.addressOptions.optionalAddressOptions.fieldName
        )
        assertEquals(
            VGSCheckoutFieldVisibility.VISIBLE,
            config.formConfig.addressOptions.optionalAddressOptions.visibility
        )

        assertEquals(
            POSTAL_CODE_FIELD_NAME,
            config.formConfig.addressOptions.postalCodeOptions.fieldName
        )
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
            PATH,
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
        assertEquals(2, config.routeConfig.requestOptions.extraHeaders.size)
        assertEquals(
            APPLICATION_JSON,
            config.routeConfig.requestOptions.extraHeaders[CONTENT_TYPE]
        )
        assertEquals(
            BEARER,
            config.routeConfig.requestOptions.extraHeaders[AUTHORIZATION]
        )

        Assert.assertTrue(config.routeConfig.requestOptions.extraData.isEmpty())
        assertEquals(
            VGSCheckoutDataMergePolicy.NESTED_JSON,
            config.routeConfig.requestOptions.mergePolicy
        )
    }

    @Test
    fun setRouteId() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .setRouteId(ID)
            .build()
        // Assert
        assertEquals(ID, config.routeId)
    }

    @Test
    fun setEnvironment() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .setEnvironment(VGSCheckoutEnvironment.Live())
            .build()
        // Assert
        assertEquals(VGSCheckoutEnvironment.Live(), config.environment)
    }

    @Test
    fun setIsScreenshotsAllowed() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .setIsScreenshotsAllowed(true)
            .build()
        // Assert
        assertEquals(true, config.isScreenshotsAllowed)
    }

    @Test
    fun setValidationBehaviour() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .setFormValidationBehaviour(VGSCheckoutFormValidationBehaviour.ON_FOCUS)
            .build()
        // Assert
        assertEquals(VGSCheckoutFormValidationBehaviour.ON_FOCUS, config.formConfig.validationBehaviour)
    }

    @Test
    fun configureBillingAddressSettings() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder(ID)
            .setAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setPostalCodeOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setOptionalAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setCountryOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setCityOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.HIDDEN)
            .setOptionalAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
            .build()

        // Assert
        assertEquals(VGSCheckoutBillingAddressVisibility.HIDDEN, config.formConfig.addressOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.addressOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.optionalAddressOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.postalCodeOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.countryOptions.visibility)
        assertEquals(VGSCheckoutFieldVisibility.HIDDEN, config.formConfig.addressOptions.cityOptions.visibility)
    }

    @Test
    fun createAddCardConfig_validJWT_objectCreated() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder("")
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .build()
        // Assert
        Assert.assertNotNull(config)
    }

    @Test
    fun createAddCardConfig_envSandboxEnabledByDefault() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder("")
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .build()
        // Assert
        assert(config.environment is VGSCheckoutEnvironment.Sandbox)
    }

    @Test
    fun createAddCardConfig_screenshotsDisabledByDefault() {
        // Act
        val config = VGSCheckoutPaymentConfig.Builder("")
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .build()
        // Assert
        Assert.assertFalse(config.isScreenshotsAllowed)
    }

//    @Test(expected = JWTParseException::class)
//    fun createAddCardConfig_emptyJWT_exceptionThrown() {
//        // Act
//        VGSCheckoutAddCardConfig("", "")
//    }

//    @Test(expected = JWTRestrictedRoleException::class)
//    fun createAddCardConfig_invalidJWT_exceptionThrown() {
//        // Act
//        VGSCheckoutAddCardConfig(BuildConfig.JWT_TOKEN_WITH_TRANSFERS_WRITE, "")
//    }

    companion object {
        private const val EXP_FIELD_NAME = "card.expDate"
        private const val CARD_HOLDER_FIELD_NAME = "card.name"
        private const val CARD_NUMBER_FIELD_NAME = "card.number"
        private const val CVC_FIELD_NAME = "card.cvc"
        private const val MONTH_FIELD_NAME = "card.exp_month"
        private const val YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_INPUT_FORMAT = "MM/yy"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/yyyy"

        private const val COUNTRY_FIELD_NAME = "card.billing_address.country"
        private const val CITY_FIELD_NAME = "card.billing_address.city"
        private const val ADDRESS1_FIELD_NAME = "card.billing_address.address1"
        private const val ADDRESS2_FIELD_NAME = "card.billing_address.address2"
        private const val POSTAL_CODE_FIELD_NAME = "card.billing_address.postal_code"

        private const val PATH = "/financial_instruments"
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer "
    }
}