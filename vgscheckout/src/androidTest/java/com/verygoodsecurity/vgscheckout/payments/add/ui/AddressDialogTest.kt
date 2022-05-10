package com.verygoodsecurity.vgscheckout.payments.add.ui

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
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
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

    val addressIntent = Intent(context, SaveCardActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(
                VGSCheckoutAddCardConfig(
                    BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                    BuildConfig.VAULT_ID,
                    formConfig = VGSCheckoutAddCardFormConfig(
                        VGSCheckoutPaymentBillingAddressOptions(
                            visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                        ),
                        VGSCheckoutFormValidationBehaviour.ON_FOCUS
                    ),
                    isScreenshotsAllowed = true
                )
            )
        )
    }

    @Test
    fun addressSetup_countrySelect_dialogShowed() {
        launch<SaveCardActivity>(addressIntent).use {
            // Act
            waitFor(500)
            onView(isRoot()).perform(ViewActions.closeSoftKeyboard())

            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            //Assert
            onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }

    @Test
    fun addressSetup_countrySelect_usaByDefault() {
        launch<SaveCardActivity>(addressIntent).use {
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("United States")))
        }
    }

    @Test
    fun addressSetup_countrySelect_zipCodeHintByDefault() {
        launch<SaveCardActivity>(addressIntent).use {
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(VGSViewMatchers.withHint("Zip code")))
        }
    }

    @Test
    fun addressSetup_countrySelect_selectCanada_countryChanged() {
        launch<SaveCardActivity>(addressIntent).use {
            // Act
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            //Assert
            onView(withId(R.id.vgsEtCountry)).check(matches(VGSViewMatchers.withText("Canada")))
        }
    }

    @Test
    fun addressSetup_countrySelect_selectCanada_postalCodeHintChanged() {
        launch<SaveCardActivity>(addressIntent).use {
            // Act
            waitFor(500)
            onViewWithScrollTo(R.id.vgsTilCountry).perform(click())
            onData(hasToString(startsWith("Canada"))).perform(scrollTo()).perform(click())
            onView(withText("Ok")).perform(click())
            waitFor(1000)
            //Assert
            onViewWithScrollTo(R.id.vgsTilPostalCode).check(matches(VGSViewMatchers.withHint("Postal code")))
        }
    }

    @Test
    fun limitCountries_setOnlyUnitedKingdom_otherCountriesDoesNotPresent() {
        // Arrange
        val validCountriesList = listOf("GB")
        val intent = getLimitCountriesIntent(validCountriesList)
        launch<SaveCardActivity>(intent).use {
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
        launch<SaveCardActivity>(intent).use {
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
        launch<SaveCardActivity>(intent).use {
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
        launch<SaveCardActivity>(intent).use {
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
        Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            VGSCheckoutPaymentBillingAddressOptions(
                                countryOptions = VGSCheckoutPaymentCountryOptions(
                                    validCountries = countries
                                ),
                                visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                            ),
                            VGSCheckoutFormValidationBehaviour.ON_FOCUS
                        ),
                    )
                )
            )
        }
}