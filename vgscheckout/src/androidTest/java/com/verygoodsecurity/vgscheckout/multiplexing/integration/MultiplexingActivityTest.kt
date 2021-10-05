package com.verygoodsecurity.vgscheckout.multiplexing.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.Constants.CHECKOUT_RESULT_CONTRACT_NAME
import com.verygoodsecurity.vgscheckout.Constants.CORRECT_TOKEN
import com.verygoodsecurity.vgscheckout.Constants.INCORRECT_TOKEN
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class MultiplexingActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfig(
                    CORRECT_TOKEN,
                    VAULT_ID,
                )
            )
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test(expected = IllegalArgumentException::class)
    fun performMultiplexing_unsuccessfulStart() {
        val intent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
            putExtra(
                CHECKOUT_RESULT_CONTRACT_NAME,
                CheckoutResultContract.Args(
                    VGSCheckoutMultiplexingConfig(
                        INCORRECT_TOKEN,
                        VAULT_ID,
                    )
                )
            )
        }
        ActivityScenario.launch<CheckoutMultiplexingActivity>(intent)
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withNavigationUp() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
            //Assert
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withBackPress() {
        ActivityScenario.launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            // Act
            device.pressBack()
            //Assert
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }
}