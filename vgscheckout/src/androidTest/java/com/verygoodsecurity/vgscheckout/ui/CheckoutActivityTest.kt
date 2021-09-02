package com.verygoodsecurity.vgscheckout.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
import com.verygoodsecurity.vgscheckout.util.ActionHelper.doAction
import com.verygoodsecurity.vgscheckout.util.Matchers.error
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
            CheckoutResultContract.Args(VGSCheckoutConfiguration("tnt3dj7zbi8"))
        )
    }

    @Test
    fun performCheckout_saveButtonIsEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.mbSaveCard)).check(matches(isEnabled()))
        }
    }

    @Test
    fun performCheckout_hidePersonName() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = "tntpszqgikn",
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
            onView(withId(R.id.vgsTilCardHolder)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun preformCheckout_noErrorMessagesDisplayedByDefault() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(matches(error(null)))
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
                    error(
                        "Name is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(
                matches(
                    error(
                        "Card number is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                matches(
                    error(
                        "Expiration date is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    error(
                        "CVC is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilAddress).check(
                matches(
                    error(
                        "Address line 1 is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilCity).check(
                matches(
                    error(
                        "City is empty"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
                matches(
                    error(
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
                    error(
                        "Enter a valid card number"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(
                matches(
                    error(
                        "Expiration date is not valid"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(
                matches(
                    error(
                        "CVC is not valid"
                    )
                )
            )
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(
                matches(
                    error(
                        "ZIP is invalid"
                    )
                )
            )
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
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(error(null)))
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(matches(error(null)))
        }
    }

    @Test
    fun performCheckout_addressIsVisibleByDefault() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.llBillingAddress)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun performCheckout_hideAddress() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = "tntpszqgikn",
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
            onView(withId(R.id.llBillingAddress)).check(matches(not(isDisplayed())))
        }
    }

    private fun fillCardFields(
        cardHolderName: String = VALID_CARD_HOLDER,
        cardNumber: String = VALID_CARD_NUMBER,
        expirationDate: String = VALID_EXP_DATE,
        cvc: String = VALID_SECURITY_CODE,
    ) {
        onView(withId(R.id.vgsEtCardHolder)).perform(doAction<PersonNameEditText> {
            it.setText(cardHolderName)
        })
        onView(withId(R.id.vgsEtCardNumber)).perform(doAction<VGSCardNumberEditText> {
            it.setText(cardNumber)
        })
        onView(withId(R.id.vgsEtExpirationDate)).perform(doAction<ExpirationDateEditText> {
            it.setText(expirationDate)
        })
        onView(withId(R.id.vgsEtSecurityCode)).perform(doAction<CardVerificationCodeEditText> {
            it.setText(cvc)
        })
    }

    private fun fillAddressFields(
        address: String = VALID_ADDRESS,
        city: String = VALID_CITY,
        postalAddress: String = VALID_POSTAL_ADDRESS
    ) {
        onView(withId(R.id.vgsEtAddress)).perform(doAction<VGSEditText> {
            it.setText(address)
        })
        onView(withId(R.id.vgsEtCity)).perform(doAction<VGSEditText> {
            it.setText(city)
        })
        onView(withId(R.id.vgsEtPostalAddress)).perform(doAction<VGSEditText> {
            it.setText(postalAddress)
        })
    }

    companion object {

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