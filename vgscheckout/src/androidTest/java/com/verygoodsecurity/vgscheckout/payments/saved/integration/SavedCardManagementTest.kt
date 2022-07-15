package com.verygoodsecurity.vgscheckout.payments.saved.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER_AMEX
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_NUMBER_MASTERCARD
import com.verygoodsecurity.vgscheckout.Constants.VALID_SECURITY_CODE_AMEX
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.VGSCheckoutSavedCardsCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutDeleteCardResponse
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.util.AccessTokenHelper
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.addCardPaymentInstrument
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class SavedCardManagementTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var token: String
    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()

        token = AccessTokenHelper.getToken()
    }

    private fun createIntent(config: VGSCheckoutAddCardConfig) =
        Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }

    private fun initializeSavedCardConfig(arrayListOf: ArrayList<String>) =
        CountDownLatch(1).runCatching {
            val savedConfig = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(token)
                .setIsScreenshotsAllowed(true)
                .build()

            savedConfig.loadSavedCard(
                context,
                arrayListOf,
                object : VGSCheckoutSavedCardsCallback {
                    override fun onSuccess() {
                        countDown()
                    }

                    override fun onFailure(exception: VGSCheckoutException) {
                        countDown()
                    }
                }
            )

            await()

            Assert.assertNotNull(savedConfig)

            savedConfig
        }.getOrNull()

    @Test
    fun performPaymentOrchestration_presaved3Card_selected2Card() {
        val amexCardPosition = 2
        val finID0 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER)
        val finID1 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER_MASTERCARD)
        val finID2 = addCardPaymentInstrument(
            context,
            token,
            VALID_CARD_NUMBER_AMEX,
            cvc = VALID_SECURITY_CODE_AMEX
        )
        val intent = initializeSavedCardConfig(arrayListOf(finID0, finID1, finID2)).run {
            createIntent(this!!)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            // Act
            onView(withId(R.id.rvPaymentMethods))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<PaymentMethodsAdapter.CardViewHolder>(
                        amexCardPosition,
                        click()
                    )
                )
            onView(withId(R.id.mbPresent)).perform(click())
            //Assert
            val id = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                ?.checkoutResult?.data?.getParcelable<VGSCheckoutCardResponse>(
                    VGSCheckoutResultBundle.Keys.ADD_CARD_RESPONSE
                )?.getId()
            Assert.assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            Assert.assertEquals(id, finID2)
        }
    }

    @Test
    fun performPaymentOrchestration_presaved3Card_delete3Cards() {
        val removedCardSize = 3
        val finID0 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER)
        val finID1 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER_MASTERCARD)
        val finID2 = addCardPaymentInstrument(
            context,
            token,
            VALID_CARD_NUMBER_AMEX,
            cvc = VALID_SECURITY_CODE_AMEX
        )
        val intent = initializeSavedCardConfig(arrayListOf(finID0, finID1, finID2)).run {
            createIntent(this!!)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            for (i in 0..2) {   //remove 3 cards
                onView(withId(R.id.delete)).perform(click())
                onView(withText(R.string.vgs_checkout_delete_dialog_positive_button_title)).perform(
                    click()
                )
                waitFor(3000)
            }

            device.pressBack()
            //Assert
            val cards = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                ?.checkoutResult?.data?.getParcelableList<VGSCheckoutDeleteCardResponse>(
                    VGSCheckoutResultBundle.Keys.DELETE_CARD_RESPONSES
                ) ?: ArrayList()
            Assert.assertTrue(cards.size == removedCardSize)
            Assert.assertEquals(cards[0].financialInstrumentId, finID0)
            Assert.assertEquals(cards[1].financialInstrumentId, finID1)
            Assert.assertEquals(cards[2].financialInstrumentId, finID2)
        }
    }

    @Test
    fun performPaymentOrchestration_loadDeletedCard() {
        val finID0 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER)
        val intent = initializeSavedCardConfig(arrayListOf(finID0)).run {
            createIntent(this!!)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            onView(withId(R.id.delete)).perform(click())
            onView(withText(R.string.vgs_checkout_delete_dialog_positive_button_title)).perform(
                click()
            )
            waitFor(5000)

            device.pressBack()
            //Assert
            val cards = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                ?.checkoutResult?.data?.getParcelableList<VGSCheckoutDeleteCardResponse>(
                    VGSCheckoutResultBundle.Keys.DELETE_CARD_RESPONSES
                ) ?: ArrayList()
            Assert.assertEquals(cards[0].financialInstrumentId, finID0)
        }

        val wrongSavedCardIntent = initializeSavedCardConfig(arrayListOf(finID0)).run {
            createIntent(this!!)
        }

        ActivityScenario.launch<SaveCardActivity>(wrongSavedCardIntent).use {
            waitFor(500)
            // Act
            onView(withId(R.id.rvPaymentMethods)).check(doesNotExist())
        }
    }

    @Test
    fun performPaymentOrchestration_presaved3Card_deleteSecondCard_submitThirdCard() {
        val secondCardPosition = 1
        val finID0 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER)
        val finID1 = addCardPaymentInstrument(context, token, VALID_CARD_NUMBER_MASTERCARD)
        val finID2 = addCardPaymentInstrument(
            context,
            token,
            VALID_CARD_NUMBER_AMEX,
            cvc = VALID_SECURITY_CODE_AMEX
        )
        val intent = initializeSavedCardConfig(arrayListOf(finID0, finID1, finID2)).run {
            createIntent(this!!)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            onView(withId(R.id.rvPaymentMethods))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<PaymentMethodsAdapter.CardViewHolder>(
                        secondCardPosition,
                        click()
                    )
                )
            onView(withId(R.id.delete)).perform(click())
            onView(withText(R.string.vgs_checkout_delete_dialog_positive_button_title)).perform(
                click()
            )
            waitFor(5000)

            onView(withId(R.id.rvPaymentMethods))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<PaymentMethodsAdapter.CardViewHolder>(
                        secondCardPosition,
                        click()
                    )
                )
            onView(withId(R.id.mbPresent)).perform(click())
            //Assert
            val deletedCards =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getParcelableList<VGSCheckoutDeleteCardResponse>(
                        VGSCheckoutResultBundle.Keys.DELETE_CARD_RESPONSES
                    ) ?: ArrayList()
            Assert.assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            Assert.assertEquals(deletedCards[0].financialInstrumentId, finID1)

            val isPreSavedCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.IS_PRE_SAVED_CARD)
                    ?: false
            Assert.assertTrue(isPreSavedCard)

            val sentCards = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                ?.checkoutResult?.data?.getParcelable<VGSCheckoutCardResponse>(
                    VGSCheckoutResultBundle.Keys.ADD_CARD_RESPONSE
                )?.getId()
            Assert.assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            Assert.assertEquals(sentCards, finID2)
        }
    }
}