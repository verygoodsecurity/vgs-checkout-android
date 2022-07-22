package com.verygoodsecurity.vgscheckout.payments.saved.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@Suppress("SameParameterValue")
@RunWith(AndroidJUnit4::class)
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

    private fun initializeSavedCardConfig() = CountDownLatch(1).runCatching {
        val finID = addCardPaymentInstrument(context, token)

        val savedConfig = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(token)
            .setIsScreenshotsAllowed(true)
            .setSavedCardsIds(arrayListOf(finID))
            .build()

        VGSCheckoutAddCardConfig.loadSavedCards(
            context,
            savedConfig,
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

        Assert.assertNotNull(savedConfig)
        Assert.assertTrue(savedConfig.savedCards.isNotEmpty())

        savedConfig
    }.getOrNull()

    private fun createIntent(config: VGSCheckoutAddCardConfig) =
        Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config)
            )
        }

    @Test
    fun performPaymentOrchestration_cancelActivityResult_withBackPress_codeCanceled() {
        val config = CountDownLatch(1).runCatching {
            val finID = addCardPaymentInstrument(context, token)
            Assert.assertTrue(finID.isEmpty())

            val savedConfig = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
                .setAccessToken(token)
                .setIsScreenshotsAllowed(true)
                .setSavedCardsIds(arrayListOf(finID))
                .build()

            VGSCheckoutAddCardConfig.loadSavedCards(
                context,
                savedConfig,
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

            savedConfig
        }.getOrNull()

        Assert.assertNotNull(config)
        Assert.assertTrue(config!!.savedCards.isNotEmpty())

        val intent = createIntent(config)

        ActivityScenario.launch<SaveCardActivity>(intent).use {
            // Act
            device.pressBack()
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Canceled)
        }
    }

    @Test
    fun performPaymentOrchestration_cancelActivityResult_withNavigationUp_codeCanceled() {
        val intent = initializeSavedCardConfig().run {
            createIntent(this!!)
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

    @Test
    fun performPaymentOrchestration_savedCard_successfulResponse_codeOk() {
        val intent = initializeSavedCardConfig().run {
            createIntent(this!!)
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

    @Test
    fun performPaymentOrchestration_isPreSavedCard_true() {
        val intent = initializeSavedCardConfig().run {
            createIntent(this!!)
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
