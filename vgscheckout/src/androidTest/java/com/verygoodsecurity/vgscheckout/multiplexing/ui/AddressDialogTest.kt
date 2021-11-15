package com.verygoodsecurity.vgscheckout.multiplexing.ui

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
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutMultiplexingFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutMultiplexingBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutMultiplexingCountryOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.waitFor
import com.verygoodsecurity.vgscheckout.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import org.hamcrest.Matchers.hasToString
import org.hamcrest.Matchers.startsWith
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddressDialogTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfig(
                    Constants.VALID_JWT_TOKEN,
                    VAULT_ID,
                )
            )
        )
    }

    @Test
    fun countrySelect_dialogShowed() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            waitFor(500)
            onView(isRoot()).perform(ViewActions.closeSoftKeyboard())

            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            //Assert
            onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    @Test
    fun countrySelect_usaByDefault() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("United States")))
        }
    }

    @Test
    fun countrySelect_zipCodeHintByDefault() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(VGSViewMatchers.withHint("Zip code")))
        }
    }

    @Test
    fun countrySelect_selectCanada_countryChanged() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun countrySelect_selectCanada_postalCodeHintChanged() {
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(VGSViewMatchers.withHint("Postal code")))
        }
    }

    @Test
    fun limitCountries_setOnlyUnitedKingdom_otherCountriesDoesNotPresent() {
        // Arrange
        val validCountriesList = listOf("GB")
        val intent = getLimitCountriesIntent(validCountriesList)
        launch<CheckoutMultiplexingActivity>(intent).use {
            // Act
            var countries: List<Country>? = null
            onView(withId(R.id.vgsEtCountry)).perform(ActionHelper.doAction<VGSCountryEditText> {
                countries = it.getCountries()
            })
            //Assert
            assert(countries?.size == validCountriesList.size)
        }
    }

    @Test
    fun limitCountries_setInvalidCountryWithValid_allValidCountriesPresent() {
        // Arrange
        val invalidCountries = listOf("USD")
        val validCountriesList = listOf("UA", "US", "GB")
        val intent = getLimitCountriesIntent(invalidCountries + validCountriesList)
        launch<CheckoutMultiplexingActivity>(intent).use {
            // Act
            var countries: List<Country>? = null
            onView(withId(R.id.vgsEtCountry)).perform(ActionHelper.doAction<VGSCountryEditText> {
                countries = it.getCountries()
            })
            //Assert
            assert(countries?.size == validCountriesList.size)
        }
    }

    @Test
    fun limitCountries_setInvalidCountry_allCountriesPresent() {
        // Arrange
        val allCountries = CountriesHelper.countries
        val invalidCountriesList = listOf("USD")
        val intent = getLimitCountriesIntent(invalidCountriesList)
        launch<CheckoutMultiplexingActivity>(intent).use {
            // Act
            var countries: List<Country>? = null
            onView(withId(R.id.vgsEtCountry)).perform(ActionHelper.doAction<VGSCountryEditText> {
                countries = it.getCountries()
            })
            //Assert
            assert(countries?.size == allCountries.size)
        }
    }

    @Test
    fun limitCountries_setEmptyCountryList_allCountriesPresent() {
        // Arrange
        val allCountries = CountriesHelper.countries
        val emptyCountriesList = listOf<String>()
        val intent = getLimitCountriesIntent(emptyCountriesList)
        launch<CheckoutMultiplexingActivity>(intent).use {
            // Act
            var countries: List<Country>? = null
            onView(withId(R.id.vgsEtCountry)).perform(ActionHelper.doAction<VGSCountryEditText> {
                countries = it.getCountries()
            })
            //Assert
            assert(countries?.size == allCountries.size)
        }
    }

    private fun getLimitCountriesIntent(countries: List<String>): Intent =
        Intent(context, CheckoutMultiplexingActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutMultiplexingConfig(
                        Constants.VALID_JWT_TOKEN,
                        VAULT_ID,
                        formConfig = VGSCheckoutMultiplexingFormConfig(
                            addressOptions = VGSCheckoutMultiplexingBillingAddressOptions(
                                countryOptions = VGSCheckoutMultiplexingCountryOptions(
                                    validCountries = countries
                                )
                            )
                        )
                    )
                )
            )
        }
}