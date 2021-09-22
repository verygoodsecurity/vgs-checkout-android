package com.verygoodsecurity.vgscheckout.checkout.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.Constants.CHECKOUT_RESULT_CONTRACT_NAME
import com.verygoodsecurity.vgscheckout.Constants.VALID_CARD_HOLDER
import com.verygoodsecurity.vgscheckout.Constants.VAULT_ID
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckoutActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(VGSCheckoutConfiguration(VAULT_ID))
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withNavigationUp() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description))
                .perform(ViewActions.click())
            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withBackPress() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            device.pressBack()
            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withContent_ok() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(VALID_CARD_HOLDER)
                })

            device.pressBack()

            Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click())
            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performMultiplexing_cancelActivityResult_withContent_cancel() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(VALID_CARD_HOLDER)
                })

            device.pressBack()
            //Assert
            Espresso.onView(ViewMatchers.withText("Cancel")).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .check(ViewAssertions.matches(VGSViewMatchers.withText(VALID_CARD_HOLDER)))
        }
    }
}