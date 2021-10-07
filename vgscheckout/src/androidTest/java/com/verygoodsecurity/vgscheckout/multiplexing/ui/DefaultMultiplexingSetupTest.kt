package com.verygoodsecurity.vgscheckout.multiplexing.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants.CORRECT_TOKEN
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
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
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfig(
                    CORRECT_TOKEN,
                    VAULT_ID,
                )
            )
        )
    }

    @Test
    fun performCheckout_defaultVisibleFields() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCardHolder))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCardNumber))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilExpirationDate))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilSecurityCode))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCountry))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddressOptional)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun performCheckout_defaultFieldContent() {
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

            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress)).check(
                ViewAssertions.matches(VGSViewMatchers.withText(""))
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddressOptional)
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional)).check(
                ViewAssertions.matches(VGSViewMatchers.withText(""))
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilAddress)
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress)).check(
                ViewAssertions.matches(VGSViewMatchers.withText(""))
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCity)
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity)).check(
                ViewAssertions.matches(VGSViewMatchers.withText(""))
            )
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress)).check(
                ViewAssertions.matches(VGSViewMatchers.withText(""))
            )
        }
    }

    @Test
    fun performCheckout_saveButtonInteractive() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        }
    }

    @Test
    fun performCheckout_noErrorMessagesDisplayed() {
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
}