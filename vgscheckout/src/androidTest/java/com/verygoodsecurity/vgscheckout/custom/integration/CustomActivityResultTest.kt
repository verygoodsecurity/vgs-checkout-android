package com.verygoodsecurity.vgscheckout.custom.integration

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
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutCustomRouteConfig
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutCustomFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCustomCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCustomCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCustomCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutCustomExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.*
import com.verygoodsecurity.vgscheckout.ui.CustomCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.ViewInteraction.onViewWithScrollTo
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.getParcelableSafe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomActivityResultTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CustomCheckoutActivity::class.java).apply {
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(VGSCheckoutCustomConfig(
                BuildConfig.VAULT_ID,
                isScreenshotsAllowed = true
            ))
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
    }

    @Test
    fun performCheckout_custom_saveCard_unsuccessfulResponse_resultFailed_codeOk() {
        // Arrange
        launch<CustomCheckoutActivity>(defaultIntent).use {
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
            onViewWithScrollTo(R.id.mbSaveCard).perform(click())
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_OK, it.result.resultCode)
            assertTrue(result?.checkoutResult is VGSCheckoutResult.Failed)
        }
    }

    @Test
    fun performCheckout_saveCard_successfulResponse_resultSuccess_codeOk() {
        // Arrange
        val intent = Intent(context, CustomCheckoutActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_ARGS,
                CheckoutResultContract.Args(
                    VGSCheckoutCustomConfig(
                        vaultId = BuildConfig.VAULT_ID,
                        VGSCheckoutEnvironment.Sandbox(),
                        VGSCheckoutCustomRouteConfig("/post"),
                        VGSCheckoutCustomFormConfig(
                            cardOptions = VGSCheckoutCustomCardOptions(
                                VGSCheckoutCustomCardNumberOptions("card.number"),
                                VGSCheckoutCustomCardHolderOptions("card.holder"),
                                VGSCheckoutCustomCVCOptions("card.cvc"),
                                VGSCheckoutCustomExpirationDateOptions("card.expDate")
                            )
                        )
                    )
                )
            )
        }
        launch<CustomCheckoutActivity>(intent).use {
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
        launch<CustomCheckoutActivity>(defaultIntent).use {
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
        launch<CustomCheckoutActivity>(defaultIntent).use {
            // Act
            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
            device.pressBack()
            //Assert
            val result = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
            assertNull(result?.checkoutResult)
        }
    }
}