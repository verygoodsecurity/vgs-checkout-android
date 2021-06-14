package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import junit.framework.Assert.assertNotNull
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    private val testIntent =
        Intent(ApplicationProvider.getApplicationContext(), CheckoutActivity::class.java).apply {
            putExtra("extra_checkout_vault_id", "tntpszqgikn")
            putExtra("extra_checkout_environment", "sandbox")
            putExtra("extra_checkout_config", VGSCheckoutVaultConfiguration.Builder().build())
        }

    @get:Rule
    internal val activityRule = ActivityScenarioRule<CheckoutActivity>(testIntent)

    @Test
    fun performCheckout_validParams_successfulResult() {
        // Act
        onView(withId(R.id.mbPay)).perform(ViewActions.click())
        Thread.sleep(5000)
        //Assert
        assertNotNull(activityRule.scenario.result)
    }

    private inline fun <reified P : View> doAction(
        description: String = "doAction",
        crossinline action: (view: P) -> Unit
    ) = object : ViewAction {

        override fun getDescription(): String = description

        override fun getConstraints(): Matcher<View> = isAssignableFrom(P::class.java)

        override fun perform(uiController: UiController?, view: View?) {
            if (view == null || view !is P) {
                throw RuntimeException("View is null or can't be casted to ${P::class.java.simpleName}")
            }
            action.invoke(view)
        }
    }
}