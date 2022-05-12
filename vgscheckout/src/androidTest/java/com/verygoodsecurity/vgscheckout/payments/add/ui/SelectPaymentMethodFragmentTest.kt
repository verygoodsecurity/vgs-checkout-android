package com.verygoodsecurity.vgscheckout.payments.add.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutPaymentRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutAddCardFormConfig
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.util.ActionHelper.doAction
import com.verygoodsecurity.vgscheckout.util.extension.safeResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelectPaymentMethodFragmentTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var device: UiDevice
    private lateinit var intent: Intent

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
        intent = Intent(context, SaveCardActivity::class.java)
    }

    @Test
    fun selectPaymentMethodFragmentShowed_cardsAndAddNewCardShowed() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, true))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            //Assert
            onView(withId(R.id.rvPaymentMethods)).perform(doAction<RecyclerView> {
                assertEquals(cards.size.inc(), it.adapter?.itemCount)
            })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_deleteExist() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, true))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            //Assert
            onView(withId(R.id.delete)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_deleteDoesNotExist() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            //Assert
            onView(withId(R.id.delete)).check(doesNotExist())
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_deleteDoesNotExistAfterAllCardsDeleted() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            onView(withId(R.id.rvPaymentMethods)).perform(doAction<RecyclerView> {
                val adapter = (it.adapter as? PaymentMethodsAdapter)
                cards.forEach { card -> adapter?.removeItem(card) }
            })
            //Assert
            onView(withId(R.id.mbPay)).perform(doAction<MaterialButton> { assertFalse(it.isEnabled) })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_payEnabled() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            //Assert
            onView(withId(R.id.mbPay)).perform(doAction<MaterialButton> { assertTrue(it.isEnabled) })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_payDisabledAfterAllCardsDeleted() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            onView(withId(R.id.rvPaymentMethods)).perform(doAction<RecyclerView> {
                val adapter = (it.adapter as? PaymentMethodsAdapter)
                cards.forEach { card -> adapter?.removeItem(card) }
            })
            //Assert
            onView(withId(R.id.mbPay)).perform(doAction<MaterialButton> { assertFalse(it.isEnabled) })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_firstCardReturnedOnPayClick() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            onView(withId(R.id.mbPay)).perform(click())
            val checkoutResult = scenario.safeResult.resultData?.getParcelableExtra<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)?.checkoutResult?.data
            val addCardResponse = checkoutResult?.getParcelable<VGSCheckoutCardResponse>(VGSCheckoutResultBundle.ADD_CARD_RESPONSE)
            //Assert
            assertEquals(Activity.RESULT_OK, scenario.safeResult.resultCode)
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_resultCancelReturnedOnBackClick() {
        // Arrange
        val cards = getCardsFixtures()
        putConfig(getConfigFixture(cards, false))
        // Act
        ActivityScenario.launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect(scenario)
            device.pressBack()
            //Assert
            assertEquals(Activity.RESULT_CANCELED, scenario.safeResult.resultCode)
        }
    }

    // TODO: add is dialog showed test
    // TODO: add new card click test

    private fun putConfig(config: CheckoutConfig) {
        intent.putExtra(EXTRA_KEY_ARGS, CheckoutResultContract.Args(config))
    }

    private fun getConfigFixture(
        cards: List<Card>,
        isRemoveCardOptionEnabled: Boolean
    ) = VGSCheckoutAddCardConfig(
        "",
        BuildConfig.VAULT_ID,
        VGSCheckoutEnvironment.Sandbox(),
        VGSCheckoutPaymentRouteConfig(""),
        VGSCheckoutAddCardFormConfig(),
        isScreenshotsAllowed = true,
        isRemoveCardOptionEnabled = isRemoveCardOptionEnabled,
        createdFromParcel = false
    ).apply { savedCards = cards }

    private fun getCardsFixtures(): List<Card> {
        val result = mutableListOf<Card>()
        (1..10).forEach {
            val id = "finId = $it"
            result.add(
                Card(
                    id,
                    "Name $it",
                    "111111111111111$it",
                    it,
                    it,
                    "VISA",
                    Card.Raw(true, 200, id, null)
                )
            )
        }
        return result
    }

    private fun checkFragmentCorrect(scenario: ActivityScenario<SaveCardActivity>) {
        scenario.onActivity {
            assertTrue(it.supportFragmentManager.findFragmentByTag(BaseCheckoutActivity.FRAGMENT_TAG) is SelectPaymentMethodFragment)
        }
    }
}