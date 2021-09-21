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

    companion object {
        private const val VAULT_ID = "tnt1a2b3c4y"

        private const val CHECKOUT_RESULT_CONTRACT_NAME =
            "com.verygoodsecurity.vgscheckout.model.extra_checkout_args"

        // Fields data
        private const val VALID_CARD_HOLDER = "John Doe"
        private const val VALID_CARD_NUMBER = "4111111111111111"
        private const val INVALID_CARD_NUMBER = "0000000000000000"
        private const val VALID_EXP_DATE = "10/22"
        private const val INVALID_EXP_DATE = "10/2"
        private const val VALID_SECURITY_CODE = "111"
        private const val INVALID_SECURITY_CODE = "11"

        private const val VALID_ADDRESS = "Somewhere st."
        private const val VALID_CITY = "New York"
        private const val VALID_POSTAL_ADDRESS = "12345"
        private const val INVALID_POSTAL_ADDRESS = "1234"
    }
}