package com.verygoodsecurity.vgscheckout.custom.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper.setText
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withParent
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.waitFor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class OnFocusFieldsValidationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private var intent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(VGSCheckoutCustomConfig(
                vaultId = BuildConfig.VAULT_ID,
                formConfig = VGSCheckoutCustomFormConfig(
                    addressOptions = VGSCheckoutCustomBillingAddressOptions(
                        visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                    ),
                    validationBehaviour = VGSCheckoutFormValidationBehaviour.ON_FOCUS
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
    fun focusChange_noInput_errorsNotDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilCardNumber, CardInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilExpirationDate, DateInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilSecurityCode, CVCInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilAddress, InfoInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilCity, InfoInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilPostalCode, InfoInputField::class))
                .perform(click())
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError(null)))

            // Act
            Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
            waitFor(500)
            device.pressBack()

            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun focusChange_emptyInput_errorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(typeText(Constants.VALID_CARD_HOLDER))
                .perform(setText<PersonNameInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilCardNumber, CardInputField::class))
                .perform(typeText(Constants.VALID_CARD_NUMBER))
                .perform(setText<CardInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilExpirationDate, DateInputField::class))
                .perform(typeText(Constants.VALID_EXP_DATE))
                .perform(setText<DateInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilSecurityCode, CVCInputField::class))
                .perform(typeText(Constants.VALID_SECURITY_CODE))
                .perform(setText<CVCInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilAddress, InfoInputField::class))
                .perform(typeText(Constants.VALID_ADDRESS))
                .perform(setText<InfoInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilCity, InfoInputField::class))
                .perform(typeText(Constants.VALID_CITY))
                .perform(setText<InfoInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilPostalCode, InfoInputField::class))
                .perform(typeText(Constants.USA_VALID_ZIP_CODE))
                .perform(setText<InfoInputField>(null))
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError("Name is empty")))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError("Card number is empty")))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError("Expiry date is empty")))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError("CVC/CVV is empty")))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(withError("Address line 1 is empty")))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(withError("City is empty")))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("ZIP is empty")))
        }
    }

    @Test
    fun focusChange_invalidInput_errorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(withParent(R.id.vgsTilCardNumber, CardInputField::class))
                .perform(typeText(Constants.INVALID_CARD_NUMBER))
            onViewWithScrollTo(withParent(R.id.vgsTilExpirationDate, DateInputField::class))
                .perform(typeText(Constants.INVALID_EXP_DATE))
            onViewWithScrollTo(withParent(R.id.vgsTilSecurityCode, CVCInputField::class))
                .perform(typeText(Constants.INVALID_SECURITY_CODE))
            onViewWithScrollTo(withParent(R.id.vgsTilPostalCode, InfoInputField::class))
                .perform(typeText(Constants.USA_INVALID_ZIP_CODE))
            onViewWithScrollTo(withParent(R.id.vgsTilCardHolder, PersonNameInputField::class))
                .perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError("Invalid card number")))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError("Invalid expiry date")))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError("Invalid CVC/CVV format")))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("ZIP is invalid")))
        }
    }

    @Test
    fun submitClicked_noInput_errorsDisplayed() {
        launch<CheckoutActivity>(intent).use {
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError("Name is empty")))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError("Card number is empty")))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError("Expiry date is empty")))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError("CVC/CVV is empty")))
            onViewWithScrollTo(R.id.vgsTilAddress).check(matches(withError("Address line 1 is empty")))
            onViewWithScrollTo(R.id.vgsTilCity).check(matches(withError("City is empty")))
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(withError("ZIP is empty")))
        }
    }
}