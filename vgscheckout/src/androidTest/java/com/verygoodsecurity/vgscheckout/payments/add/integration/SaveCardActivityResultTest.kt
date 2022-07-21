package com.verygoodsecurity.vgscheckout.payments.add.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.ui.CustomSaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
class SaveCardActivityResultTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, SaveCardActivity::class.java).apply {
        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
            .setIsScreenshotsAllowed(true)
            .build()
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(config)
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
    }

    @Test(timeout = 60000L)
    fun performCheckout_PaymentOrchestration_saveCard_unsuccessfulResponse_resultFailed_codeOk() {
        //Arrange
        launch<SaveCardActivity>(defaultIntent).use {
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
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Failed)
        }
    }

    @Test
    fun performPaymentOrchestration_cancelActivityResult_withNavigationUp_codeCanceled() {
        launch<SaveCardActivity>(defaultIntent).use {
            waitFor(500)
            // Act
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
            //Assert
            val result = it.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Canceled)
        }
    }

    @Test
    fun performPaymentOrchestration_cancelActivityResult_withBackPress_codeCanceled() {
        launch<SaveCardActivity>(defaultIntent).use {
            // Act
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
            device.pressBack()
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Canceled)
        }
    }

    @Test(timeout = 60000L)
    fun performPaymentOrchestration_saveCard_isPreSavedCard_true() {
        //Arrange
        launch<SaveCardActivity>(defaultIntent).use {
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
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val isPreSavedCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.IS_PRE_SAVED_CARD)
                    ?: false
            assertFalse(isPreSavedCard)
        }
    }


    @Test
    fun performPaymentOrchestration_saveCardOptionEnabled_default() {
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setIsScreenshotsAllowed(true)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }
        // Arrange
        launch<CustomSaveCardActivity>(intent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val shouldSaveCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.SHOULD_SAVE_CARD)
                    ?: false
            assertTrue(shouldSaveCard)
        }
    }

    @Test
    fun performPaymentOrchestration_saveCardOptionEnabled_disable() {
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setIsScreenshotsAllowed(true)
                .setIsSaveCardOptionVisible(true)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }
        // Arrange
        launch<CustomSaveCardActivity>(intent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            // Act
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val shouldSaveCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.SHOULD_SAVE_CARD)
                    ?: false
            assertFalse(shouldSaveCard)
        }
    }

    @Test
    fun performPaymentOrchestration_saveCardOptionEnabled_deselectedByUser() {
        val intent = Intent(context, SaveCardActivity::class.java).apply {
            val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(BuildConfig.JWT_TOKEN_WITHOUT_TRANSFERS)
                .setIsScreenshotsAllowed(true)
                .build()
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }
        // Arrange
        launch<CustomSaveCardActivity>(intent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            // Act
            onView(ViewMatchers.withId(R.id.mcbSaveCard)).perform(click())
            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val isPreSavedCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.SHOULD_SAVE_CARD)
                    ?: false
            assertFalse(isPreSavedCard)
        }
    }
}