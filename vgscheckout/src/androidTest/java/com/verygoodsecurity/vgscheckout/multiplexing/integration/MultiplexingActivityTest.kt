package com.verygoodsecurity.vgscheckout.multiplexing.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import org.junit.Assert.assertEquals
import org.junit.Before
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
                    CORRECT_TOKEN
                )
            )
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test(expected = IllegalArgumentException::class)
    fun performMultiplexing_unsuccessfulStart() {
        val intent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
            putExtra(
                CHECKOUT_RESULT_CONTRACT_NAME,
                CheckoutResultContract.Args(
                    VGSCheckoutMultiplexingConfiguration(
                        VAULT_ID,
                        INCORRECT_TOKEN
                    )
                )
            )
        }
        ActivityScenario.launch<CheckoutMultiplexingActivity>(intent)
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withNavigationUp() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
            //Assert
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withBackPress() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            device.pressBack()
            //Assert
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withContent_ok() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(VALID_CARD_HOLDER)
                })

            device.pressBack()

            onView(withText("OK")).perform(click())
            //Assert
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withContent_cancel() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(VALID_CARD_HOLDER)
                })

            device.pressBack()
            //Assert
            onView(withText("Cancel")).perform(click())
            onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .check(ViewAssertions.matches(VGSViewMatchers.withText(VALID_CARD_HOLDER)))
        }
    }

    companion object {
        private const val VAULT_ID = "tnt1a2b3c4y"
        private const val INCORRECT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50MWEyYjNjNHkiOiJURVNUIn19.5rtUsrLnS_gBHKTugiM9h9wuI9t4afupSbbdgmFHbSU"
        private const val CORRECT_TOKEN =
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