package com.verygoodsecurity.vgscheckout.multiplexing.ui


import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.verygoodsecurity.vgscheckout.Constants.CHECKOUT_RESULT_CONTRACT_NAME
import com.verygoodsecurity.vgscheckout.Constants.CORRECT_TOKEN
import com.verygoodsecurity.vgscheckout.Constants.INVALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.INVALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.INVALID_POSTAL_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.INVALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.VALID_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_HOLDER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CITY
import com.verygoodsecurity.vgscheckout.Constants.VALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.VALID_POSTAL_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.VALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.fillAddressFields
import com.verygoodsecurity.vgscheckout.util.extension.fillCardFields
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class FieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfiguration(
                    CORRECT_TOKEN,
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
                        "Expiration date is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    withError(
                        "CVC is empty"
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
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
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
                INVALID_POSTAL_ADDRESS
            )
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                matches(
                    withError(
                        "Enter a valid card number"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                matches(
                    withError(
                        "Expiration date is not valid"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    withError(
                        "CVC is not valid"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
                matches(
                    withError(
                        "ZIP is invalid"
                    )
                )
            )
        }
    }

    @Test
    fun countrySelect_dialogShowed() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            //Assert
            onView(ViewMatchers.isRoot())
                .inRoot(RootMatchers.isDialog()).check(matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onView(ViewMatchers.withText("Canada")).perform(click())
            onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            onView(ViewMatchers.withId(R.id.vgsEtCountry))
                .check(matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressHintChanged() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onView(ViewMatchers.withText("Canada")).perform(click())
            onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(VGSViewMatchers.withHint("Postal Code")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressErrorChanged() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onView(ViewMatchers.withText("Canada")).perform(click())
            onView(ViewMatchers.withText("Ok")).perform(click())
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(withError("Postal code is empty")))
        }
    }

    //todo edit this test flow
    @Test
    fun showErrorMessage_countrySelect_selectCanada_postalAddressErrorMessageCleared() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onView(ViewMatchers.withText("Canada")).perform(click())
            onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(withError(null)))
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
                VALID_POSTAL_ADDRESS
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
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(matches(withError(null)))
        }
    }
}