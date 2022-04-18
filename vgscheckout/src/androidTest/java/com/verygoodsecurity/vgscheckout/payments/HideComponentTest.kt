package com.verygoodsecurity.vgscheckout.payments

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCustomCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutPaymentCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPaymentPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CustomSaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HideComponentTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun performCheckout_addressBlockHiddenByDefault() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideCountryField() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                countryOptions = VGSCheckoutPaymentCountryOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCountry))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideAddressField() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                addressOptions = VGSCheckoutPaymentAddressOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideOptionalAddressField() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                optionalAddressOptions = VGSCheckoutPaymentOptionalAddressOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilAddressOptional))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideCityField() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                cityOptions = VGSCheckoutPaymentCityOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilCity))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hidePostalAddressField() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                postalCodeOptions = VGSCheckoutPaymentPostalCodeOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.vgsTilPostalCode))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideAllField_addressBlockHidden() {
        // Arrange
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutAddCardConfig(
                        BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS,
                        BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutAddCardFormConfig(
                            addressOptions = VGSCheckoutPaymentBillingAddressOptions(
                                countryOptions = VGSCheckoutPaymentCountryOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN,
                                    validCountries = listOf("YE")
                                ),
                                addressOptions = VGSCheckoutPaymentAddressOptions(visibility = VGSCheckoutFieldVisibility.HIDDEN),
                                optionalAddressOptions = VGSCheckoutPaymentOptionalAddressOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                ),
                                cityOptions = VGSCheckoutPaymentCityOptions(visibility = VGSCheckoutFieldVisibility.HIDDEN),
                                visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun performCheckout_countryWithoutPostalCode_addressBlockHidden() {
        // Arrange
        val intent = Intent(context, CustomSaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutCustomConfig(
                        vaultId = BuildConfig.VAULT_ID,
                        formConfig = VGSCheckoutCustomFormConfig(
                            addressOptions =
                            VGSCheckoutCustomBillingAddressOptions(
                                countryOptions = VGSCheckoutCustomCountryOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN,
                                    validCountries = listOf("YE")
                                ),
                                addressOptions = VGSCheckoutCustomAddressOptions(visibility = VGSCheckoutFieldVisibility.HIDDEN),
                                optionalAddressOptions = VGSCheckoutCustomOptionalAddressOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                ),
                                cityOptions = VGSCheckoutCustomCityOptions(visibility = VGSCheckoutFieldVisibility.HIDDEN),
                                visibility = VGSCheckoutBillingAddressVisibility.VISIBLE
                            )
                        )
                    )
                )
            )
        }
        ActivityScenario.launch<CustomSaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }
}