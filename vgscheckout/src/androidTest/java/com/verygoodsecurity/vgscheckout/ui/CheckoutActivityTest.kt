package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import junit.framework.Assert.assertNotNull
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
}