package com.verygoodsecurity.vgscheckout.payments.add.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.Constants.INVALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.INVALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.INVALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.Constants.USA_VALID_ZIP_CODE
import com.verygoodsecurity.vgscheckout.Constants.VALID_ADDRESS
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_HOLDER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CITY
import com.verygoodsecurity.vgscheckout.Constants.VALID_EXP_DATE
import com.verygoodsecurity.vgscheckout.Constants.VALID_SECURITY_CODE
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.PersonNameInputField
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.*
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasToString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class OnSubmitFieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val intent = Intent(context, SaveCardActivity::class.java).apply {
        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .setIsScreenshotsAllowed(true)
            .build()
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(config)
        )
    }

    val addressIntent = Intent(context, SaveCardActivity::class.java).apply {
        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .setIsScreenshotsAllowed(true)
            .setFormValidationBehaviour(VGSCheckoutFormValidationBehaviour.ON_FOCUS)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.VISIBLE)
            .build()
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(config)
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
        launch<SaveCardActivity>(intent).use {
            // Act
            onViewWithScrollTo(VGSViewMatchers.withParent(R.id.vgsTilCardNumber,
                CardInputField::class))
                .perform(ViewActions.typeText("4111"))
            onViewWithScrollTo(VGSViewMatchers.withParent(R.id.vgsTilCardHolder,
                PersonNameInputField::class))
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError(null)))
        }
    }

    @Test
    fun addressSetup_saveCardClicked_noInput_emptyErrorsDisplayed() {
        launch<SaveCardActivity>(addressIntent).use {
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
    fun defaultSetup_saveCardClicked_invalidInput_invalidInputErrorsDisplayed() {
        launch<SaveCardActivity>(intent).use {
            // Arrange
            waitFor(500)
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())

            fillCardFields(
                VALID_CARD_HOLDER,
                INVALID_CARD_NUMBER,
                INVALID_EXP_DATE,
                INVALID_SECURITY_CODE
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
        }
    }

    @Test
    fun addressSetup_saveCard_custom_validInput_noErrorsDisplayed() {
        launch<SaveCardActivity>(addressIntent).use {
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
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
            it.onActivity { activity ->
                (activity.findFragmentByTag(BaseCheckoutActivity.FRAGMENT_TAG) as? SaveCardFragment)?.shouldHandleAddCard = false
            }

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
    fun addressSetup_showErrorMessage_countrySelect_selectCanada_postalCodeErrorMessageCleared() {
        launch<SaveCardActivity>(addressIntent).use {
            // Act
            waitFor(500)
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())

            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(Matchers.startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError(null)))
        }
    }

    @Test
    fun addressSetup_noError_selectCanada_postalCodeValidationRuleChange_errorDisplayed() {
        launch<SaveCardActivity>(addressIntent).use {
            // Arrange
            waitFor(500)
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())

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
            onData(hasToString(Matchers.startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())

            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("Postal code is invalid")))
        }
    }
}