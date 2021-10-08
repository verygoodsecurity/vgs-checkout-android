package com.verygoodsecurity.vgscheckout.custom.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressDialogTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(VGSCheckoutCustomConfig(VAULT_ID))
        )
    }

    @Test
    fun countrySelect_dialogShowed() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            //Assert
            Espresso.onView(ViewMatchers.isRoot())
                .inRoot(RootMatchers.isDialog()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCountry))
                .check(ViewAssertions.matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressHintChanged() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilCountry).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Canada")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withText("Ok")).perform(ViewActions.click())
            //Assert
            ViewInteraction.onViewWithScrollTo(R.id.vgsTilPostalAddress)
                .check(ViewAssertions.matches(VGSViewMatchers.withHint("Postal code")))
        }
    }
}