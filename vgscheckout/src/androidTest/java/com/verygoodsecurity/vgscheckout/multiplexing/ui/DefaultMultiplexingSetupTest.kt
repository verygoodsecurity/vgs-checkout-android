package com.verygoodsecurity.vgscheckout.multiplexing.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class DefaultMultiplexingSetupTest {

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
    fun performCheckoutFieldPresets() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCountry))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("United States")))

            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress).also {
                Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress)).check(
                    ViewAssertions.matches(VGSViewMatchers.withText(""))
                )
            }
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddressOptional).also {
                Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional)).check(
                    ViewAssertions.matches(VGSViewMatchers.withText(""))
                )
            }
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress).also {
                Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress)).check(
                    ViewAssertions.matches(VGSViewMatchers.withText(""))
                )
            }
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity).also {
                Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity)).check(
                    ViewAssertions.matches(VGSViewMatchers.withText(""))
                )
            }
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress).also {
                Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress)).check(
                    ViewAssertions.matches(VGSViewMatchers.withText(""))
                )
            }
        }
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
    fun preformCheckout_noErrorMessagesDisplayed() {
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
    fun performCheckout_addressIsVisible() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
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