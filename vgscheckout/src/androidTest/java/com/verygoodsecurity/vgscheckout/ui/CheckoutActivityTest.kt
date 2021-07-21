package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.util.ActionHelper.doAction
import com.verygoodsecurity.vgscheckout.util.extension.readExtraParcelable
import com.verygoodsecurity.vgscheckout.util.extension.safeResult
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
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
    fun performCheckout_cardHolderNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCardHolder)
            setText(R.id.vgsEtCardHolder, TEST_TEXT)
            clearFocus(R.id.vgsEtCardHolder)
            setText(R.id.vgsEtCardHolder, EMPTY)

            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Name is empty", errorText)
        }
    }

    @Test
    fun performCheckout_cardNumberEmpty_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCardNumber)
            setText(R.id.vgsEtCardNumber, TEST_TEXT)
            setText(R.id.vgsEtCardNumber, EMPTY)
            clearFocus(R.id.vgsEtCardNumber)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Card number is empty", errorText)
        }
    }

    @Test
    fun performCheckout_cardNumberNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCardNumber)
            setText(R.id.vgsEtCardNumber, TEST_TEXT)
            clearFocus(R.id.vgsEtCardNumber)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Enter a valid card number", errorText)
        }
    }

    @Test
    fun performCheckout_expirationDateEmpty_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtDate)
            setText(R.id.vgsEtDate, TEST_TEXT)
            setText(R.id.vgsEtDate, EMPTY)
            clearFocus(R.id.vgsEtDate)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Expiration date is empty", errorText)
        }
    }

    @Test
    fun performCheckout_expirationDateNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtDate)
            setText(R.id.vgsEtDate, TEST_TEXT)
            clearFocus(R.id.vgsEtDate)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Expiration date is not valid", errorText)
        }
    }

    @Test
    fun performCheckout_cvcEmpty_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCVC)
            setText(R.id.vgsEtCVC, TEST_TEXT)
            setText(R.id.vgsEtCVC, EMPTY)
            clearFocus(R.id.vgsEtCVC)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Security code is empty", errorText)
        }
    }

    @Test
    fun performCheckout_cvcNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCVC)
            setText(R.id.vgsEtCVC, TEST_TEXT)
            clearFocus(R.id.vgsEtCVC)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Security code is not valid", errorText)
        }
    }

    @Test
    fun performCheckout_fieldsNotValid_errorOrderCorrect() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            requestFocus(R.id.vgsEtCardNumber)
            setText(R.id.vgsEtCardNumber, TEST_TEXT)
            requestFocus(R.id.vgsEtCardHolder)
            setText(R.id.vgsEtCardHolder, TEST_TEXT)
            setText(R.id.vgsEtCardHolder, EMPTY)
            clearFocus(R.id.vgsEtCardHolder)
            var errorText: String? = null
            onView(withId(R.id.tvCardDetailsError)).perform(doAction<MaterialTextView> {
                errorText = it.text.toString()
            })
            //Assert
            assertEquals("Name is empty", errorText)
        }
    }

    @Test
    fun performCheckout_validFilledFields_fieldsNotEnabledWhenLoading() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            onView(withId(R.id.mbPay)).perform(scrollTo(), ViewActions.click())
            //Assert
            onView(withId(R.id.vgsEtCardHolder)).check(matches(not(isEnabled())))
            onView(withId(R.id.vgsEtCardNumber)).check(matches(not(isEnabled())))
            onView(withId(R.id.vgsEtDate)).check(matches(not(isEnabled())))
            onView(withId(R.id.vgsEtCVC)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_hideCardHolderName() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setFormConfig(
                        VGSCheckoutFormConfiguration.Builder()
                            .setCardOptions(
                                VGSCheckoutCardOptions.Builder()
                                    .setCardHolderOptions(
                                        VGSCheckoutCardHolderOptions.Builder()
                                            .setVisibility(VGSCheckoutFieldVisibility.GONE)
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.llCardHolder)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun performCheckout_showCardHolderName() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setFormConfig(
                        VGSCheckoutFormConfiguration.Builder()
                            .setCardOptions(
                                VGSCheckoutCardOptions.Builder()
                                    .setCardHolderOptions(
                                        VGSCheckoutCardHolderOptions.Builder()
                                            .setVisibility(VGSCheckoutFieldVisibility.VISIBLE)
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.llCardHolder)).check(matches(isDisplayed()))
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
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setFormConfig(
                        VGSCheckoutFormConfiguration.Builder()
                            .setAddressOptions(
                                VGSCheckoutAddressOptions.Builder()
                                    .setAddressFormVisibility(VGSCheckoutFieldVisibility.GONE)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields()
            //Assert
            onView(withId(R.id.mtvAddressTitle)).check(matches(not(isDisplayed())))
            onView(withId(R.id.addressView)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun performCheckout_validParams_successfulResult() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setRouteConfig(
                        VGSCheckoutRouteConfiguration.Builder()
                            .setPath("post")
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use { scenario ->
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            // Act
            onView(withId(R.id.mbPay)).perform(scrollTo(), ViewActions.click())
            val result = scenario.safeResult
            val vgsResultData =
                result.resultData?.readExtraParcelable<VGSCheckoutResult>(CHECKOUT_RESULT_EXTRA_KEY)
            //Assert
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals(SUCCESS_CODE, vgsResultData?.code)
        }
    }

    @Test
    fun performCheckout_invalidHTTPMethod_errorReturned() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setRouteConfig(
                        VGSCheckoutRouteConfiguration.Builder()
                            .setPath("post")
                            .setRequestOptions(
                                VGSCheckoutRequestOptions.Builder()
                                    .setHTTPMethod(VGSCheckoutHTTPMethod.DELETE)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use { scenario ->
            fillCardFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            // Act
            onView(withId(R.id.mbPay)).perform(scrollTo(), ViewActions.click())
            val result = scenario.safeResult
            val vgsResultData =
                result.resultData?.readExtraParcelable<VGSCheckoutResult>(CHECKOUT_RESULT_EXTRA_KEY)
            //Assert
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals(METHOD_NOT_ALLOWED, vgsResultData?.code)
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
        onView(withId(R.id.vgsEtDate)).perform(doAction<ExpirationDateEditText> {
            it.setText(expirationDate)
        })
        onView(withId(R.id.vgsEtCVC)).perform(doAction<CardVerificationCodeEditText> {
            it.setText(cvc)
        })
    }

    private fun setText(@IdRes id: Int, text: String) {
        onView(withId(id)).perform(doAction<InputFieldView> {
            it.setText(text)
        })
    }

    private fun requestFocus(@IdRes id: Int) {
        onView(withId(id)).perform(doAction<InputFieldView> {
            it.requestFocus()
        })
    }

    private fun clearFocus(@IdRes id: Int) {
        onView(withId(id)).perform(doAction<InputFieldView> {
            it.clearFocus()
        })
    }

    companion object {

        // Fields data
        private const val TEST_TEXT = "1"
        private const val EMPTY = ""
        private const val CARD_HOLDER_NAME = "Test"
        private const val CARD_NUMBER = "4111111111111111"
        private const val INVALID_CARD_NUMBER = "4000000000000000"
        private const val CARD_EXPIRATION_DATE = "10/22"
        private const val INVALID_CARD_EXPIRATION_DATE = "01/21"
        private const val CARD_CVC = "111"
        private const val INVALID_CARD_CVC = "11"

        // Response codes
        private const val SUCCESS_CODE = 200
        private const val METHOD_NOT_ALLOWED = 405
    }
}