package com.verygoodsecurity.vgscheckout.custom.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CustomSaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withError
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.waitFor
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultCheckoutSetupTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CustomSaveCardActivity::class.java).apply {
        val config = VGSCheckoutCustomConfig.Builder(BuildConfig.VAULT_ID)
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
    fun performCheckout_defaultVisibleFields() {
        launch<SaveCardActivity>(defaultIntent).use {
            waitFor(1500)
            //Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilCardNumber)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilExpirationDate)
                .check(matches(isDisplayed()))
            onViewWithScrollTo(R.id.vgsTilSecurityCode)
                .check(matches(isDisplayed()))

            onView(withId(R.id.vgsTilCountry)).check(matches(not(isCompletelyDisplayed())))
            onView(withId(R.id.vgsTilAddressOptional)).check(matches(not(isCompletelyDisplayed())))
            onView(withId(R.id.vgsTilAddress)).check(matches(not(isCompletelyDisplayed())))
            onView(withId(R.id.vgsTilCity)).check(matches(not(isCompletelyDisplayed())))
            onView(withId(R.id.vgsTilPostalCode)).check(matches(not(isCompletelyDisplayed())))

            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun performCheckout_defaultFieldContent() {
        launch<CustomSaveCardActivity>(defaultIntent).use {
            //Act
            onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
            //Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder)
            onView(withId(R.id.vgsEtCardHolder))
                .check(matches(VGSViewMatchers.withText("")))
            onViewWithScrollTo(R.id.vgsTilCardNumber)
            onView(withId(R.id.vgsEtCardNumber))
                .check(matches(VGSViewMatchers.withText("")))
            onViewWithScrollTo(R.id.vgsTilExpirationDate)
            onView(withId(R.id.vgsEtExpirationDate))
                .check(matches(VGSViewMatchers.withText("")))
            onViewWithScrollTo(R.id.vgsTilSecurityCode)
            onView(withId(R.id.vgsEtSecurityCode))
                .check(matches(VGSViewMatchers.withText("")))
        }
    }

    @Test
    fun performCheckout_saveButtonInteractive() {
        launch<SaveCardActivity>(defaultIntent).use {
            //Assert
            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isEnabled()))
            onViewWithScrollTo(R.id.mbSaveCard)
                .check(matches(isClickable()))
        }
    }

    @Test
    fun preformCheckout_noErrorMessagesDisplayed() {
        launch<CustomSaveCardActivity>(defaultIntent).use {
            waitFor(500)
            onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
            // Assert
            onViewWithScrollTo(R.id.vgsTilCardHolder).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilCardNumber).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilExpirationDate).check(matches(withError(null)))
            onViewWithScrollTo(R.id.vgsTilSecurityCode).check(matches(withError(null)))
        }
    }

    @Test
    fun performCheckout_addressIsVisible() {
        launch<CustomSaveCardActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.llBillingAddress)).check(matches(not(isCompletelyDisplayed())))
        }
    }
}