package com.verygoodsecurity.vgscheckout.checkout.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class FieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(VGSCheckoutConfiguration(VAULT_ID))
        )
    }

    @Test
    fun performCheckout_hidePersonName() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                CHECKOUT_RESULT_CONTRACT_NAME,
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = VAULT_ID,
                        formConfig = VGSCheckoutFormConfiguration(
                            cardOptions = VGSCheckoutCardOptions(
                                cardHolderOptions = VGSCheckoutCardHolderOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCardHolder))
                .check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun saveCard_noInput_emptyErrorsDisplayed() {
        launch<CheckoutActivity>(defaultIntent).use {
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
        launch<CheckoutActivity>(defaultIntent).use {
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
    fun performCheckout_hideAddress() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                CHECKOUT_RESULT_CONTRACT_NAME,
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = VAULT_ID,
                        formConfig = VGSCheckoutFormConfiguration(
                            addressOptions =
                            VGSCheckoutBillingAddressOptions(
                                visibility =
                                VGSCheckoutBillingAddressVisibility.HIDDEN
                            )
                        )
                    )
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun countrySelect_dialogShowed() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            //Assert
            Espresso.onView(ViewMatchers.isRoot())
                .inRoot(RootMatchers.isDialog()).check(matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCountry))
                .check(matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressHintChanged() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(VGSViewMatchers.withHint("Postal Code")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressErrorChanged() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(click())
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(withError("Postal code is empty")))
        }
    }

    //todo edit this test flow
    @Test
    fun showErrorMessage_countrySelect_selectCanada_postalAddressErrorMessageCleared() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(withError(null)))
        }
    }

    @Test
    fun saveCard_validInput_noErrorsDisplayed() {
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