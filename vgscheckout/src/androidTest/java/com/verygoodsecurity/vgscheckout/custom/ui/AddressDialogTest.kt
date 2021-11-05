package com.verygoodsecurity.vgscheckout.custom.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import org.hamcrest.Matchers.hasToString
import org.hamcrest.Matchers.startsWith
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
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            //Assert
            onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    @Test
    fun countrySelect_usaByDefault() {
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("United States")))
        }
    }

    @Test
    fun countrySelect_zipCodeHintByDefault() {
        launch<CheckoutActivity>(defaultIntent).use {
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(matches(VGSViewMatchers.withHint("Zip code")))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry)
            onView(withId(R.id.vgsTilCountry)).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalAddressHintChanged() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalAddress).check(matches(VGSViewMatchers.withHint("Postal code")))
        }
    }
}