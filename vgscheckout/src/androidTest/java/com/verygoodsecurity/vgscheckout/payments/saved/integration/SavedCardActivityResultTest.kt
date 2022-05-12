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
import com.verygoodsecurity.vgscheckout.util.AccessTokenHelper
import com.verygoodsecurity.vgscheckout.VGSCheckoutConfigInitCallback
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
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

    private lateinit var intent: Intent
    private lateinit var token: String
    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()

        token = AccessTokenHelper.getToken()
        val config = initializeSavedCardConfig()

        intent = Intent(context, SaveCardActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(config!!)
            )
        }
    }

    /*
    - додать 3 картки і перевірить чі всі є
    - додать 3 картки і перевірить вибрану
        - повиділять нові картки.

    - додать 3 картки і видалить 2гу. перевірить чі правильно видалилася
        - перевірити чі виділена потрібна
    - додать 2 картки і видалить одну. спробувать її ше раз загрузить.
    - додать 3 картки і видалить 2.
        - перевірить у респонсах їх.

    - перевірить SHOULD_SAVE_CARD
     */
    private fun initializeSavedCardConfig() = CountDownLatch(1).runCatching {
        val finID = addCardPaymentInstrument(context, token)
        var savedConfig: VGSCheckoutAddCardConfig? = null
        VGSCheckoutAddCardConfig.create(
            context,
            token,
            BuildConfig.VAULT_ID,
            VGSCheckoutPaymentMethod.SavedCards(arrayListOf(finID)),
            //todo remove test after testing
            //VGSCheckoutPaymentMethod.SavedCards(arrayListOf("FNdUL83yeMt9RWN6uePVD5kj")),
            isScreenshotsAllowed = true,
            callback = object : VGSCheckoutConfigInitCallback<VGSCheckoutAddCardConfig> {
                override fun onSuccess(config: VGSCheckoutAddCardConfig) {
                    savedConfig = config
                    countDown()
                }

                override fun onFailure(exception: VGSCheckoutException) {}
            }
        )
        await()

        Assert.assertNotNull(savedConfig)

        savedConfig
    }.getOrNull()

    @Test
    fun performPaymentOrchestration_cancelActivityResult_withBackPress_codeCanceled() {
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
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.mbPay)).perform(ViewActions.click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            Assert.assertEquals(Activity.RESULT_OK, it.safeResult.resultCode)
            Assert.assertTrue(result?.checkoutResult is VGSCheckoutResult.Success)
        }
    }

    @Test
    fun performPaymentOrchestration_isPreSavedCard_true() {
        ActivityScenario.launch<SaveCardActivity>(intent).use {
            waitFor(500)
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.mbPay)).perform(ViewActions.click())
            //Assert
            val isPreSavedCard =
                it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
                    ?.checkoutResult?.data?.getBoolean(VGSCheckoutResultBundle.Keys.IS_PRE_SAVED_CARD)
                    ?: false
            Assert.assertTrue(isPreSavedCard)
        }
    }
}
