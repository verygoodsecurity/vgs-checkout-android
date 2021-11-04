package com.verygoodsecurity.vgscheckout.multiplexing.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.VGSViewMatchers.withText
import com.verygoodsecurity.vgscheckout.util.extension.fillAddressFields
import com.verygoodsecurity.vgscheckout.util.extension.fillCardFields
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScreenRotationTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutMultiplexingActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(
                VGSCheckoutMultiplexingConfig(
                    Constants.VALID_JWT_TOKEN,
                    Constants.VAULT_ID,
                )
            )
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun screenRotation_inputNotCleared() {
        // Arrange
        launch<CheckoutMultiplexingActivity>(defaultIntent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            fillAddressFields(
                Constants.VALID_ADDRESS,
                Constants.VALID_CITY,
                Constants.USA_VALID_ZIP_CODE
            )
            // Act
            device.setOrientationLeft()
            //Assert
            onView(withId(R.id.vgsEtCardHolder)).check(matches(withText(Constants.VALID_CARD_HOLDER)))
            onView(withId(R.id.vgsEtCardNumber)).check(matches(withText(Constants.FORMATTED_CARD_NUMBER)))
            onView(withId(R.id.vgsEtExpirationDate)).check(matches(withText(Constants.VALID_EXP_DATE)))
            onView(withId(R.id.vgsEtSecurityCode)).check(matches(withText(Constants.VALID_SECURITY_CODE)))
            onView(withId(R.id.vgsEtAddress)).check(matches(withText(Constants.VALID_ADDRESS)))
            onView(withId(R.id.vgsEtCity)).check(matches(withText(Constants.VALID_CITY)))
            onView(withId(R.id.vgsEtPostalCode)).check(matches(withText(Constants.USA_VALID_ZIP_CODE)))
        }
    }
}