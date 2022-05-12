package com.verygoodsecurity.vgscheckout.payments.add.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
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
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutEnvironment
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
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
        putConfig(getConfigFixture(getCardsFixtures(), true))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            //Assert
            onView(withId(R.id.rvPaymentMethods)).perform(doAction<RecyclerView> {
                assertEquals(cards.size.inc(), it.adapter?.itemCount)
            })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_deleteExist() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures(), true))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            //Assert
            onView(withId(R.id.delete)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_deleteDoesNotExist() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures(), false))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
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
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
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
        putConfig(getConfigFixture(getCardsFixtures(), false))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
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
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            onView(withId(R.id.rvPaymentMethods)).perform(doAction<RecyclerView> {
                val adapter = (it.adapter as? PaymentMethodsAdapter)
                cards.forEach { card -> adapter?.removeItem(card) }
            })
            //Assert
            onView(withId(R.id.mbPay)).perform(doAction<MaterialButton> { assertFalse(it.isEnabled) })
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_screenClosedWithOkOnPayClick() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures(), false))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            onView(withId(R.id.mbPay)).perform(click())
            //Assert
            assertEquals(Activity.RESULT_OK, scenario.safeResult.resultCode)
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_screenClosedWithCancelOnBackClick() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures(), false))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            device.pressBack()
            //Assert
            assertEquals(Activity.RESULT_CANCELED, scenario.safeResult.resultCode)
        }
    }

    @Test
    fun selectPaymentMethodFragmentShowed_dialogShowed() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures(), true))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)
            onView(withId(R.id.delete)).perform(click())
            //Assert
            onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        }
    }


    @Test
    fun selectPaymentMethodFragmentShowed_newCardFragmentOpened() {
        // Arrange
        putConfig(getConfigFixture(getCardsFixtures().subList(0, 1), true))
        // Act
        launch<SaveCardActivity>(intent).use { scenario ->
            checkFragmentCorrect<SelectPaymentMethodFragment>(scenario)

            onView(withId(R.id.tvPayWithNewCard)).perform(click())
            //Assert
            checkFragmentCorrect<SaveCardFragment>(scenario)
        }
    }

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

    private inline fun <reified F : Fragment> checkFragmentCorrect(scenario: ActivityScenario<SaveCardActivity>) {
        scenario.onActivity {
            assertTrue(it.supportFragmentManager.findFragmentByTag(BaseCheckoutActivity.FRAGMENT_TAG) is F)
        }
    }
}