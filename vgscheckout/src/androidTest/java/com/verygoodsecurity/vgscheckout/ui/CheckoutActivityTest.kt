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
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
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
            "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
            CheckoutResultContract.Args(
                VGSCheckoutConfiguration("tntpszqgikn")
            )
        )
    }

    @Test
    fun performCheckout_saveButtonIsEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.mbSaveCard)).check(matches(isEnabled()))
        }
    }

    @Test
    fun performCheckout_addressIsVisibleByDefault() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.llBillingAddress)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun performCheckout_hidePersonName() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = "tntpszqgikn",
                        formConfig = VGSCheckoutFormConfiguration(
                            cardOptions = VGSCheckoutCardOptions(
                                cardHolderOptions = VGSCheckoutCardHolderOptions(
                                    visibility = VGSCheckoutFieldVisibility.HIDDEN
                                )
                            )
                        )
                    )
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.vgsTilCardHolder)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun performCheckout_hideAddress() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "com.verygoodsecurity.vgscheckout.model.extra_checkout_args",
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = "tntpszqgikn",
                        formConfig = VGSCheckoutFormConfiguration(
                            addressOptions =
                            VGSCheckoutBillingAddressOptions(
                                visibility =
                                VGSCheckoutBillingAddressVisibility.HIDDEN

                            )
                        )
                    )
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.llBillingAddress)).check(matches(not(isDisplayed())))
        }
    }

    private fun fillCardFields(
        cardHolderName: String = EMPTY,
        cardNumber: String = EMPTY,
        expirationDate: String = EMPTY,
        cvc: String = EMPTY,
    ) {
        onView(withId(R.id.vgsEtCardHolder)).perform(doAction<PersonNameEditText> {
            it.setText(cardHolderName)
        })
        onView(withId(R.id.vgsEtCardNumber)).perform(doAction<VGSCardNumberEditText> {
            it.setText(cardNumber)
        })
        onView(withId(R.id.vgsEtExpirationDate)).perform(doAction<ExpirationDateEditText> {
            it.setText(expirationDate)
        })
        onView(withId(R.id.vgsEtSecurityCode)).perform(doAction<CardVerificationCodeEditText> {
            it.setText(cvc)
        })
    }

    companion object {

        // Fields data
        private const val EMPTY = ""
    }
}