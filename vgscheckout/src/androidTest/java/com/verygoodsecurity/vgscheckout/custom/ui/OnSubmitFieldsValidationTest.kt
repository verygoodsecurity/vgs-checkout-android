package com.verygoodsecurity.vgscheckout.custom.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.Constants.INVALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.INVALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.INVALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.USA_INVALID_ZIP_CODE
import com.verygoodsecurity.vgscheckout.Constants.USA_VALID_ZIP_CODE
import com.verygoodsecurity.vgscheckout.Constants.VALID_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_HOLDER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CITY
import com.verygoodsecurity.vgscheckout.Constants.VALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.VALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.PersonNameInputField
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_RESULT
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withParent
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.hamcrest.Matchers.hasToString
import org.hamcrest.Matchers.startsWith
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class OnSubmitFieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private var intent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(VGSCheckoutCustomConfig(
                VAULT_ID,
                formConfig = VGSCheckoutCustomFormConfig(
                    addressOptions = VGSCheckoutCustomBillingAddressOptions(
                        visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                    )
                ),
                isScreenshotsAllowed = true
            ))
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
    }

    @Test
    fun validationFlow_staticByDefault() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(withParent(R.id.vgsTilCardNumber, CardInputField::class))
                .perform(typeText("4111"))
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError("")))
        }
    }

    @Test
    fun saveCardClicked_noInput_emptyErrorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(R.id.mbSaveCard)
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(
                matches(
                    withError(
                        "Name is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                matches(
                    withError(
                        "Card number is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                matches(
                    withError(
                        "Expiry date is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    withError(
                        "CVC/CVV is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilAddress).check(
                matches(
                    withError(
                        "Address line 1 is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilCity).check(
                matches(
                    withError(
                        "City is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(
                matches(
                    withError(
                        "ZIP is empty"
                    )
                )
            )
        }
    }

    @Test
    fun saveCardClicked_invalidInput_invalidInputErrorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Arrange
            waitFor(500)
            onView(isRoot()).perform(closeSoftKeyboard())

            fillCardFields(
                VALID_CARD_HOLDER,
                INVALID_CARD_NUMBER,
                INVALID_EXP_DATE,
                INVALID_SECURITY_CODE
            )
            fillAddressFields(
                VALID_ADDRESS,
                VALID_CITY,
                USA_INVALID_ZIP_CODE
            )
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                matches(
                    withError(
                        "Invalid card number"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                matches(
                    withError(
                        "Invalid expiry date"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    withError(
                        "Invalid CVC/CVV format"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(
                matches(
                    withError(
                        "ZIP is invalid"
                    )
                )
            )
        }
    }

    @Test
    fun saveCard_custom_validInput_noErrorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Arrange
            fillCardFields(
                VALID_CARD_HOLDER,
                VALID_CARD_NUMBER,
                VALID_EXP_DATE,
                VALID_SECURITY_CODE
            )
            fillAddressFields(
                VALID_ADDRESS,
                VALID_CITY,
                USA_VALID_ZIP_CODE
            )
            // Act
            onView(isRoot()).perform(closeSoftKeyboard())
            waitFor(500)
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(withError("")))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("")))

            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_OK, it.result.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Success)
        }
    }

    @Test
    fun showErrorMessage_countrySelect_selectCanada_postalCodeErrorMessageCleared() {
        launch<CheckoutActivity>(intent).use {
            // Act
            waitFor(500)
            onView(isRoot()).perform(closeSoftKeyboard())

            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError(null)))
        }
    }

    @Test
    fun noError_selectCanada_postalCodeValidationRuleChange_errorDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Arrange
            waitFor(500)
            onView(isRoot()).perform(closeSoftKeyboard())

            fillCardFields(
                VALID_CARD_HOLDER,
                VALID_CARD_NUMBER,
                VALID_EXP_DATE,
                VALID_SECURITY_CODE
            )
            fillAddressFields(
                VALID_ADDRESS,
                VALID_CITY,
                USA_VALID_ZIP_CODE
            )
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())

            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("Postal code is invalid")))
        }
    }
}