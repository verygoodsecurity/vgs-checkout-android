package com.verygoodsecurity.vgscheckout.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.ActionHelper.doAction
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            "extra_checkout_config",
            VGSCheckoutConfiguration.Builder("tntpszqgikn").build()
        )
    }

    @Test
    fun performCheckout_emptyFields_payButtonIsNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_cardHolderNotValid_payButtonNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                EMPTY,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_cardNumberNotValid_payButtonIsNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                CARD_HOLDER_NAME,
                INVALID_CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_expirationDateNotValid_payButtonIsNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                INVALID_CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_cvcNotValid_payButtonIsNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                INVALID_CARD_CVC
            )
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_validFilledFields_payButtonIsEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            onView(withId(R.id.mbPay)).check(matches(isEnabled()))
        }
    }

    @Test
    fun performCheckout_addressIsVisibleByDefault() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn").build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.mtvAddressTitle)).check(matches(isDisplayed()))
            onView(withId(R.id.addressView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun performCheckout_hideAddress() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration.Builder("tntpszqgikn")
                        .setFormConfig(
                            VGSCheckoutFormConfiguration.Builder()
                                .setAddressOptions(
                                    VGSCheckoutBillingAddressOptions.Builder()
                                        .setAddressFormVisibility(
                                            VGSCheckoutBillingAddressVisibility.HIDDEN
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.mtvAddressTitle)).check(matches(not(isDisplayed())))
            onView(withId(R.id.addressView)).check(matches(not(isDisplayed())))
        }
    }

    private fun fillCardFields(
        cardHolderName: String = EMPTY,
        cardNumber: String = EMPTY,
        expirationDate: String = EMPTY,
        cvc: String = EMPTY,
    ) {
        onView(withId(R.id.vgsEtCardHolder)).perform(doAction<VGSEditText> {
            it.setText(cardHolderName)
        })
        onView(withId(R.id.vgsEtCardNumber)).perform(doAction<VGSCardNumberEditText> {
            it.setText(cardNumber)
        })
        onView(withId(R.id.vgsEtExpirationDate)).perform(doAction<ExpirationDateEditText> {
            it.setText(expirationDate)
        })
        onView(withId(R.id.vgsEtCVC)).perform(doAction<CardVerificationCodeEditText> {
            it.setText(cvc)
        })
    }

    companion object {

        // Fields data
        private const val EMPTY = ""
        private const val CARD_HOLDER_NAME = "Test"
        private const val CARD_NUMBER = "4111111111111111"
        private const val INVALID_CARD_NUMBER = "4000000000000000"
        private const val CARD_EXPIRATION_DATE = "10/22"
        private const val INVALID_CARD_EXPIRATION_DATE = "01/21"
        private const val CARD_CVC = "111"
        private const val INVALID_CARD_CVC = "11"
    }
}