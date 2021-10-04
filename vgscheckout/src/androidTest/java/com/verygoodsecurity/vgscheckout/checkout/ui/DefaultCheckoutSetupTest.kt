package com.verygoodsecurity.vgscheckout.checkout.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants.CHECKOUT_RESULT_CONTRACT_NAME
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultCheckoutSetupTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(VGSCheckoutCustomConfiguration(VAULT_ID))
        )
    }

    @Test
    fun performCheckout_defaultVisibleFields() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.vgsTilCardHolder))
                .check(matches(isDisplayed()))
            onView(withId(R.id.vgsTilCardNumber))
                .check(matches(isDisplayed()))
            onView(withId(R.id.vgsTilExpirationDate))
                .check(matches(isDisplayed()))
            onView(withId(R.id.vgsTilSecurityCode))
                .check(matches(isDisplayed()))
            onView(withId(R.id.vgsTilCountry))
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilAddress)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilAddressOptional)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilAddress)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilCity)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(matches(isDisplayed()))

            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun performCheckout_defaultFieldContent() {
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.vgsEtCardHolder))
                .check(matches(VGSViewMatchers.withText("")))
            onView(withId(R.id.vgsEtCardNumber))
                .check(matches(VGSViewMatchers.withText("")))
            onView(withId(R.id.vgsEtExpirationDate))
                .check(matches(VGSViewMatchers.withText("")))
            onView(withId(R.id.vgsEtSecurityCode))
                .check(matches(VGSViewMatchers.withText("")))
            onView(withId(R.id.vgsEtCountry))
                .check(matches(VGSViewMatchers.withText("United States")))

            onViewWithScrollTo(R.id.vgsTilAddress)
            onView(withId(R.id.vgsEtAddress)).check(
                matches(VGSViewMatchers.withText(""))
            )
            onViewWithScrollTo(R.id.vgsTilAddressOptional)
            onView(withId(R.id.vgsEtAddressOptional)).check(
                matches(VGSViewMatchers.withText(""))
            )
            onViewWithScrollTo(R.id.vgsTilAddress)
            onView(withId(R.id.vgsEtAddress)).check(
                matches(VGSViewMatchers.withText(""))
            )
            onViewWithScrollTo(R.id.vgsTilCity)
            onView(withId(R.id.vgsEtCity)).check(
                matches(VGSViewMatchers.withText(""))
            )
            onViewWithScrollTo(R.id.vgsTilPostalAddress)
            onView(withId(R.id.vgsEtPostalAddress)).check(
                matches(VGSViewMatchers.withText(""))
            )
        }
    }

    @Test
    fun performCheckout_saveButtonInteractive() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isEnabled()))
            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isClickable()))
        }
    }

    @Test
    fun preformCheckout_noErrorMessagesDisplayed() {
        launch<CheckoutActivity>(defaultIntent).use {
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

    @Test
    fun performCheckout_addressIsVisible() {
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.llBillingAddress)).check(matches(isDisplayed()))
        }
    }
}