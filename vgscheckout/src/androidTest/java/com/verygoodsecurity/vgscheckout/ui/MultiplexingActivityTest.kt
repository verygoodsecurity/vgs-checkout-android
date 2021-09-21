package com.verygoodsecurity.vgscheckout.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class MultiplexingActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfiguration(
                    VAULT_ID,
                    TOKEN
                )
            )
        )
    }


    @Test
    fun performCheckout_saveButtonIsEnabled() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.mbSaveCard))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        }
    }

    @Test
    fun preformMultiplexing_noErrorMessagesDisplayedByDefault() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardHolder)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
        }
    }

    @Test
    fun saveCard_noInput_emptyErrorsDisplayed() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
            // Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardHolder).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Name is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Card number is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Expiration date is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "CVC is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Address line 1 is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "City is empty"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "ZIP is empty"
                    )
                )
            )
        }
    }

    @Test
    fun saveCard_invalidInput_invalidInputErrorsDisplayed() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
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
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
            // Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Enter a valid card number"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "Expiration date is not valid"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "CVC is not valid"
                    )
                )
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
                ViewAssertions.matches(
                    VGSViewMatchers.withError(
                        "ZIP is invalid"
                    )
                )
            )
        }
    }

    @Test
    fun saveCard_validInput_noErrorsDisplayed() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
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
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
            // Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardHolder)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
        }
    }

    @Test
    fun performMultiplexing_addressIsVisibleByDefault() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun countrySelect_dialogShowed() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            //Assert
            Espresso.onView(ViewMatchers.isRoot())
                .inRoot(RootMatchers.isDialog())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCountry))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressHintChanged() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withHint("Postal Code")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressErrorChanged() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError("Postal code is empty")))
        }
    }

    @Test
    fun showErrorMessage_countrySelect_selectCanada_postalAddressErrorMessageCleared() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
        }
    }

    private fun fillCardFields(
        cardHolderName: String = VALID_CARD_HOLDER,
        cardNumber: String = VALID_CARD_NUMBER,
        expirationDate: String = VALID_EXP_DATE,
        cvc: String = VALID_SECURITY_CODE,
    ) {
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
            .perform(ActionHelper.doAction<PersonNameEditText> {
                it.setText(cardHolderName)
            })
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
            .perform(ActionHelper.doAction<VGSCardNumberEditText> {
                it.setText(cardNumber)
            })
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
            .perform(ActionHelper.doAction<ExpirationDateEditText> {
                it.setText(expirationDate)
            })
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
            .perform(ActionHelper.doAction<CardVerificationCodeEditText> {
                it.setText(cvc)
            })
    }

    private fun fillAddressFields(
        address: String = VALID_ADDRESS,
        city: String = VALID_CITY,
        postalAddress: String = VALID_POSTAL_ADDRESS
    ) {
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress))
            .perform(ActionHelper.doAction<VGSEditText> {
                it.setText(address)
            })
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity))
            .perform(ActionHelper.doAction<VGSEditText> {
                it.setText(city)
            })
        Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress))
            .perform(ActionHelper.doAction<VGSEditText> {
                it.setText(postalAddress)
            })
    }

    companion object {
        private const val VAULT_ID = "tnt1a2b3c4y"
        private const val TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50MWEyYjNjNHkiOnsicm9sZXMiOlsiZmluYW5jaWFsLWluc3RydW1lbnRzOndyaXRlIl19fX0.n7uQ77pOMtBY99iGVg_EtXBUsgO5GZXEKSTv4kchov0"

        private const val CHECKOUT_RESULT_CONTRACT_NAME =
            "com.verygoodsecurity.vgscheckout.model.extra_checkout_args"

        // Fields data
        private const val VALID_CARD_HOLDER = "John Doe"
        private const val VALID_CARD_NUMBER = "4111111111111111"
        private const val INVALID_CARD_NUMBER = "0000000000000000"
        private const val VALID_EXP_DATE = "10/22"
        private const val INVALID_EXP_DATE = "10/2"
        private const val VALID_SECURITY_CODE = "111"
        private const val INVALID_SECURITY_CODE = "11"

        private const val VALID_ADDRESS = "Somewhere st."
        private const val VALID_CITY = "New York"
        private const val VALID_POSTAL_ADDRESS = "12345"
        private const val INVALID_POSTAL_ADDRESS = "1234"
    }
}