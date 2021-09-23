package com.verygoodsecurity.vgscheckout.checkout.integration

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
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutActivityResultTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val defaultIntent = Intent(context, CheckoutActivity::class.java).apply {
        putExtra(
            Constants.CHECKOUT_RESULT_CONTRACT_NAME,
            CheckoutResultContract.Args(VGSCheckoutConfiguration(Constants.VAULT_ID))
        )
    }

    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun performCheckout_saveCard_unsuccessfulResponse_resultOk() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(Constants.VALID_CARD_HOLDER)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
                .perform(ActionHelper.doAction<VGSCardNumberEditText> {
                    it.setText(Constants.VALID_CARD_NUMBER)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
                .perform(ActionHelper.doAction<ExpirationDateEditText> {
                    it.setText(Constants.VALID_EXP_DATE)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
                .perform(ActionHelper.doAction<CardVerificationCodeEditText> {
                    it.setText(Constants.VALID_SECURITY_CODE)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_ADDRESS)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_ADDRESS)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_CITY)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_POSTAL_ADDRESS)
                })

            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .perform(ViewActions.click())

            //Assert
            Assert.assertEquals(Activity.RESULT_OK, it.result.resultCode)
        }
    }

    @Test
    fun performCheckout_saveCard_successfulResponse_resultOk() {
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                Constants.CHECKOUT_RESULT_CONTRACT_NAME,
                CheckoutResultContract.Args(
                    VGSCheckoutConfiguration(
                        vaultID = Constants.VAULT_ID_3,
                        "sandbox",
                        VGSCheckoutRouteConfiguration("/post"),
                        VGSCheckoutFormConfiguration(
                            cardOptions = VGSCheckoutCardOptions(
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
        ActivityScenario.launch<CheckoutActivity>(intent).use {
            // Act
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
                .perform(ActionHelper.doAction<PersonNameEditText> {
                    it.setText(Constants.VALID_CARD_HOLDER)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
                .perform(ActionHelper.doAction<VGSCardNumberEditText> {
                    it.setText(Constants.VALID_CARD_NUMBER)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
                .perform(ActionHelper.doAction<ExpirationDateEditText> {
                    it.setText(Constants.VALID_EXP_DATE)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
                .perform(ActionHelper.doAction<CardVerificationCodeEditText> {
                    it.setText(Constants.VALID_SECURITY_CODE)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_ADDRESS)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_ADDRESS)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_CITY)
                })
            Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress))
                .perform(ActionHelper.doAction<VGSEditText> {
                    it.setText(Constants.VALID_POSTAL_ADDRESS)
                })

            ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard)
                .perform(ViewActions.click())

            //Assert
            Assert.assertEquals(Activity.RESULT_OK, it.result.resultCode)
        }
    }

    @Test
    fun performCheckout_cancelActivityResult_withNavigationUp_resultCancel() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description))
                .perform(ViewActions.click())
            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }

    @Test
    fun performCheckout_cancelActivityResult_withBackPress_resultCancel() {
        ActivityScenario.launch<CheckoutActivity>(defaultIntent).use {
            // Act
            device.pressBack()
            //Assert
            Assert.assertEquals(Activity.RESULT_CANCELED, it.result.resultCode)
        }
    }
}