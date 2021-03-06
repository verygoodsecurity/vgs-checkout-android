package com.verygoodsecurity.vgscheckout.payments.add.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import com.verygoodsecurity.vgscheckout.util.extension.waitFor
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class DefaultCheckoutSetupTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, SaveCardActivity::class.java).apply {
        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .setIsScreenshotsAllowed(true)
            .build()
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(config)
        )
    }

    @Test
    fun performCheckout_defaultVisibleFields() {
        ActivityScenario.launch<SaveCardActivity>(defaultIntent).use {
            waitFor(1500)
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardHolder)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCountry))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isCompletelyDisplayed())))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilAddressOptional))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isCompletelyDisplayed())))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isCompletelyDisplayed())))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCity))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isCompletelyDisplayed())))
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilPostalCode))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isCompletelyDisplayed())))

            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun performCheckout_defaultFieldContent() {
        ActivityScenario.launch<SaveCardActivity>(defaultIntent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("")))
        }
    }

    @Test
    fun performCheckout_saveButtonInteractive() {
        ActivityScenario.launch<SaveCardActivity>(defaultIntent).use {
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        }
    }

    @Test
    fun performCheckout_noErrorMessagesDisplayed() {
        ActivityScenario.launch<SaveCardActivity>(defaultIntent).use {
            waitFor(500)
            Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
            // Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardHolder)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCardNumber)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilExpirationDate)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilSecurityCode)
                .check(ViewAssertions.matches(VGSViewMatchers.withError(null)))
        }
    }
}