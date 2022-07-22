package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.*
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_ARGS
import com.verygoodsecurity.vgscheckout.model.EXTRA_KEY_RESULT
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.ui.SaveCardActivity
import com.verygoodsecurity.vgscheckout.util.ActionHelper
import com.verygoodsecurity.vgscheckout.util.ViewInteraction
import org.hamcrest.Matcher
import org.junit.Assert

/**
 * Used to fix activity scenario get result race condition.
 *
 * Read more here: https://github.com/android/android-test/issues/676.
 */
val ActivityScenario<*>.safeResult: Instrumentation.ActivityResult
    get() {
        awaitBlock { state == Lifecycle.State.DESTROYED } // await for the activity to be destroyed
        return this.result // this will return quick as the result is already retrieved
    }

// util function to retry and await until the block is true or the timeout is reached
private fun awaitBlock(timeOut: Int = 10_000, block: () -> Boolean) {
    val start = System.currentTimeMillis()
    var value = block.invoke()
    while (!value && System.currentTimeMillis() < start + timeOut) {
        Thread.sleep(50)
        value = block.invoke()
    }
    Assert.assertTrue("Couldn't await the condition", value)
}

fun fillCardFields(
    cardHolderName: String = Constants.VALID_CARD_HOLDER,
    cardNumber: String = Constants.VALID_CARD_NUMBER,
    expirationDate: String = Constants.VALID_EXP_DATE,
    cvc: String = Constants.VALID_SECURITY_CODE,
) {
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardHolder))
        .perform(ActionHelper.doAction<PersonNameInputField> {
            it.setText(cardHolderName)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
        .perform(ActionHelper.doAction<CardInputField> {
            it.setText(cardNumber)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
        .perform(ActionHelper.doAction<DateInputField> {
            it.setText(expirationDate)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
        .perform(ActionHelper.doAction<CVCInputField> {
            it.setText(cvc)
        })
}

fun fillAddressFields(
    address: String = Constants.VALID_ADDRESS,
    city: String = Constants.VALID_CITY,
    postalCode: String = Constants.USA_VALID_ZIP_CODE
) {
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress))
        .perform(ActionHelper.doAction<InfoInputField> {
            it.setText(address)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional))
        .perform(ActionHelper.doAction<InfoInputField> {
            it.setText(Constants.VALID_ADDRESS)
        })

    Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity))
        .perform(ActionHelper.doAction<InfoInputField> {
            it.setText(city)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalCode))
        .perform(ActionHelper.doAction<InfoInputField> {
            it.setText(postalCode)
        })
}

internal inline fun <reified T : Parcelable> ActivityScenario<*>.getParcelableSafe(key: String): T? {
    val extras = safeResult.resultData?.extras
    extras?.classLoader = T::class.java.classLoader
    return extras?.getParcelable(key)
}

fun waitFor(milliseconds: Long) {
    Espresso.onView(ViewMatchers.isRoot()).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "Wait for $milliseconds milliseconds."
        }

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadForAtLeast(milliseconds)
        }

    })
}

internal fun addCardPaymentInstrument(
    context: Context,
    token: String,
    cardNumber: String = Constants.VALID_CARD_NUMBER,
    cardHolderName: String = Constants.VALID_CARD_HOLDER,
    expDate: String = Constants.VALID_EXP_DATE,
    cvc: String = Constants.VALID_SECURITY_CODE
): String {
    val intent = Intent(context, SaveCardActivity::class.java).apply {
        val config = VGSCheckoutAddCardConfig.Builder(BuildConfig.VAULT_ID)
            .setAccessToken(token)
            .setIsScreenshotsAllowed(true)
            .setBillingAddressVisibility(VGSCheckoutBillingAddressVisibility.HIDDEN)
            .build()
        putExtra(
            EXTRA_KEY_ARGS,
            CheckoutResultContract.Args(config)
        )
    }
    ActivityScenario.launch<SaveCardActivity>(intent).use {
        fillCardFields(
            cardHolderName,
            cardNumber,
            expDate,
            cvc
        )
        // Act
        ViewInteraction.onViewWithScrollTo(R.id.mbSaveCard).perform(ViewActions.click())
        //Assert
        val body = it?.getParcelableSafe<CheckoutResultContract.Result>(EXTRA_KEY_RESULT)
            ?.checkoutResult?.data?.getParcelable<VGSCheckoutCardResponse>(
                VGSCheckoutResultBundle.Keys.ADD_CARD_RESPONSE
            )
        val id = body?.getId() ?: ""

        Assert.assertTrue(id.isNotEmpty())

        return id
    }
}