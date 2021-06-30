package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.util.ActionHelper.doAction
import com.verygoodsecurity.vgscheckout.util.extension.readExtraParcelable
import com.verygoodsecurity.vgscheckout.util.extension.safeResult
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
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
            fillFields()
            //Assert
            onView(withId(R.id.mbPay)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun performCheckout_invalidFilledFields_payButtonIsNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
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
    fun performCheckout_cardHolderNotValid_payButtonNotEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
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
    fun performCheckout_cardNumberNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
                CARD_HOLDER_NAME,
                INVALID_CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            // TODO: Add assertion
        }
    }

    @Test
    fun performCheckout_expirationDateNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                INVALID_CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            //Assert
            // TODO: Add assertion
        }
    }

    @Test
    fun performCheckout_cvcNotValid_errorShowed() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                INVALID_CARD_CVC
            )
            //Assert
            // TODO: Add assertion
        }
    }

    @Test
    fun performCheckout_validFilledFields_payButtonIsEnabled() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
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
    fun performCheckout_validFilledFields_fieldsNotEnabledWhenLoading() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            onView(withId(R.id.mbPay)).perform(ViewActions.click())
            //Assert
            onView(withId(R.id.vgsEtCardHolder)).check(matches(not(isEnabled())))
            onView(withId(R.id.vgsEtCardNumber)).check(matches(not(isEnabled())))
            onView(withId(R.id.vgsEtExpirationDate)).check(matches(not(isEnabled())))
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
                            .setCardHolderOptions(
                                VGSCheckoutCardHolderOptions.Builder()
                                    .setVisibility(VGSCheckoutFieldVisibility.GONE)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillFields()
            //Assert
            onView(withId(R.id.llCardHolder)).check(matches(not(isDisplayed())))
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
            fillFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            // Act
            onView(withId(R.id.mbPay)).perform(ViewActions.click())
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
            fillFields(
                CARD_HOLDER_NAME,
                CARD_NUMBER,
                CARD_EXPIRATION_DATE,
                CARD_CVC
            )
            // Act
            onView(withId(R.id.mbPay)).perform(ViewActions.click())
            val result = scenario.safeResult
            val vgsResultData =
                result.resultData?.readExtraParcelable<VGSCheckoutResult>(CHECKOUT_RESULT_EXTRA_KEY)
            //Assert
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals(METHOD_NOT_ALLOWED, vgsResultData?.code)
        }
    }

    private fun fillFields(
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

        // Response codes
        private const val SUCCESS_CODE = 200
        private const val METHOD_NOT_ALLOWED = 405
    }
}