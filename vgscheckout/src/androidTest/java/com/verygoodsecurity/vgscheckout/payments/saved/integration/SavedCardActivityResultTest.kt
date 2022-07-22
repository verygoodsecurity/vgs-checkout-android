package com.verygoodsecurity.vgscheckout.payments.saved.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.util.AccessTokenHelper
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.addCardPaymentInstrument
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.junit.Assert
import org.junit.Before
import java.util.concurrent.CountDownLatch

@Suppress("SameParameterValue")
class SavedCardActivityResultTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var token: String
    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()

        token = AccessTokenHelper.getToken()
    }

    private fun initializeSavedCardConfig(finID: String): VGSCheckoutAddCardConfig {

        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(token)
            .setIsScreenshotsAllowed(true)
            .setSavedCardsIds(arrayListOf(finID))
            .build()

        CountDownLatch(1).runCatching {
            VGSCheckoutAddCardConfig.loadSavedCards(
                context,
                config,
                object : VGSCheckoutConfigInitCallback {
                    override fun onSuccess() {
                        countDown()
                    }

                    override fun onFailure(exception: VGSCheckoutException) {
                        countDown()
                    }
                }
            )
            await()
        }

        return config
    }

    private fun createIntent(config: VGSCheckoutAddCardConfig) =
        Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }

    fun performPaymentOrchestration_cancelActivityResult_withBackPress_codeCanceled() {
        val finID = addCardPaymentInstrument(context, token)
        Assert.assertTrue(finID.isNotEmpty())

        val intent = initializeSavedCardConfig(finID).run {
            createIntent(this)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            // Act
            device.pressBack()
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Canceled)
        }
    }

    fun performPaymentOrchestration_cancelActivityResult_withNavigationUp_codeCanceled() {
        val finID = addCardPaymentInstrument(context, token)
        Assert.assertTrue(finID.isNotEmpty())
        val intent = initializeSavedCardConfig(finID).run {
            createIntent(this)
        }

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description))
                .perform(ViewActions.click())
            //Assert
            val result = it.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Canceled)
        }
    }

    fun performPaymentOrchestration_savedCard_successfulResponse_codeOk() {
        val finID = addCardPaymentInstrument(context, token)
        Assert.assertTrue(finID.isNotEmpty())
        val intent = initializeSavedCardConfig(finID).run {
            createIntent(this)
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.mbPresent)).perform(ViewActions.click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Success)
        }
    }

    fun performPaymentOrchestration_isPreSavedCard_true() {
        val finID = addCardPaymentInstrument(context, token)
        Assert.assertTrue(finID.isNotEmpty())
        val intent = initializeSavedCardConfig(finID).run {
            createIntent(this)
        }
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.mbPresent)).perform(ViewActions.click())
            //Assert
            val isPreSavedCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.IS_PRE_SAVED_CARD)
                    ?: false
            Assert.assertTrue(isPreSavedCard)
        }
    }
}
