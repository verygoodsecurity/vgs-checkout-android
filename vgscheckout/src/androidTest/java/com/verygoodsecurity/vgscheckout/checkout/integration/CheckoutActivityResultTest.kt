package com.verygoodsecurity.vgscheckout.checkout.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.fillAddressFields
import com.verygoodsecurity.vgscheckout.util.extension.fillCardFields
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutActivityResultTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(VGSCheckoutCustomConfig(Constants.VAULT_ID_3))
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test(timeout = 60000L)
    fun performCheckout_saveCard_unsuccessfulResponse_resultFailed_codeOk() {
        // Arrange
        launch<CheckoutActivity>(defaultIntent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            fillAddressFields(
                Constants.VALID_ADDRESS,
                Constants.VALID_CITY,
                Constants.VALID_POSTAL_ADDRESS
            )
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_OK, it.result.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Failed)
        }
    }

    @Test(timeout = 60000L)
    fun performCheckout_saveCard_successfulResponse_resultSuccess_codeOk() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutCustomConfig(
                        vaultID = Constants.VAULT_ID_3,
                        VGSCheckoutEnvironment.Sandbox(),
                        VGSCheckoutCustomRouteConfig("/post"),
                        VGSCheckoutCustomFormConfig(
                            cardOptions = VGSCheckoutCustomCardOptions(
                                VGSCheckoutCardNumberOptions("card_number"),
                                VGSCheckoutCardHolderOptions("card_holder"),
                                VGSCheckoutCVCOptions("card_cvc"),
                                VGSCheckoutExpirationDateOptions("card_expDate")
                            )
                        )
                    )
                )
            )
        }
        launch<CheckoutActivity>(intent).use {
            fillCardFields(
                Constants.VALID_CARD_HOLDER,
                Constants.VALID_CARD_NUMBER,
                Constants.VALID_EXP_DATE,
                Constants.VALID_SECURITY_CODE
            )
            fillAddressFields(
                Constants.VALID_ADDRESS,
                Constants.VALID_CITY,
                Constants.VALID_POSTAL_ADDRESS
            )
            // Act
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_OK, it.result.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Success)
            assertEquals(
                Constants.SUCCESS_RESPONSE_CODE,
                (result?.checkoutResult as? VGSCheckoutResult.Success)?.code
            )
        }
    }

    @Test
    fun performCheckout_cancelActivityResult_withNavigationUp_codeCancel() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
            //Assert
            val result = it.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            assertNull(result?.checkoutResult)
        }
    }

    @Test
    fun performCheckout_cancelActivityResult_withBackPress_codeCancel() {
        launch<CheckoutActivity>(defaultIntent).use {
            // Act
            device.pressBack()
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            assertNull(result?.checkoutResult)
        }
    }
}