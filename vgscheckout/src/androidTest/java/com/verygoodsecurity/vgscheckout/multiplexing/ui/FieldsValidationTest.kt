package com.verygoodsecurity.vgscheckout.multiplexing.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants.VALID_JWT_TOKEN
import com.verygoodsecurity.vgscheckout.Constants.INVALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.INVALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.USA_INVALID_ZIP_CODE
import com.verygoodsecurity.vgscheckout.Constants.INVALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.USA_VALID_ZIP_CODE
import com.verygoodsecurity.vgscheckout.Constants.VALID_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_HOLDER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CITY
import com.verygoodsecurity.vgscheckout.Constants.VALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.VALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.fillAddressFields
import com.verygoodsecurity.vgscheckout.util.extension.fillCardFields
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matchers.hasToString
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class FieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfig(
                    VALID_JWT_TOKEN,
                    VAULT_ID,
                )
            )
        )
    }

    @Test
    fun saveCard_noInput_emptyErrorsDisplayed() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
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
    fun saveCard_invalidInput_invalidInputErrorsDisplayed() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Arrange
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
    fun saveCard_validInput_noErrorsDisplayed() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
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
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError(null)))
        }
    }

    @Test
    fun showErrorMessage_countrySelect_selectCanada_postalCodeErrorMessageCleared() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
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
        launch<CheckoutActivity>(defaultIntent).use {
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
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())

            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("Postal code is invalid")))
        }
    }
}