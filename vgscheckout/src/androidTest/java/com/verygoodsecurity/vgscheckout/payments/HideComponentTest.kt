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
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setCountryOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setOptionalAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setCityOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setPostalCodeOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.VISIBLE)
                .setCityOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .setOptionalAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .setAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .setCountryOptions(VGSCheckoutFieldVisibility.HIDDEN, listOf("YE"))
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
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
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.VISIBLE)
                .setCountryOptions(VGSCheckoutFieldVisibility.HIDDEN, listOf("YE"))
                .setCityOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .setOptionalAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .setAddressOptions(VGSCheckoutFieldVisibility.HIDDEN)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            //Assert
            Espresso.onView(ViewMatchers.withId(R.id.llBillingAddress))
                .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
        }
    }
}