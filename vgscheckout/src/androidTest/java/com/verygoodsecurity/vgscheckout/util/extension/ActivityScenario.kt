package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Instrumentation
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.verygoodsecurity.vgscheckout.Constants
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.util.ActionHelper
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
        .perform(ActionHelper.doAction<PersonNameEditText> {
            it.setText(cardHolderName)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtCardNumber))
        .perform(ActionHelper.doAction<VGSCardNumberEditText> {
            it.setText(cardNumber)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtExpirationDate))
        .perform(ActionHelper.doAction<ExpirationDateEditText> {
            it.setText(expirationDate)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtSecurityCode))
        .perform(ActionHelper.doAction<CardVerificationCodeEditText> {
            it.setText(cvc)
        })
}

fun fillAddressFields(
    address: String = Constants.VALID_ADDRESS,
    city: String = Constants.VALID_CITY,
    postalAddress: String = Constants.USA_VALID_POSTAL_ADDRESS
) {
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddress))
        .perform(ActionHelper.doAction<VGSEditText> {
            it.setText(address)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtAddressOptional))
        .perform(ActionHelper.doAction<VGSEditText> {
            it.setText(Constants.VALID_ADDRESS)
        })

    Espresso.onView(ViewMatchers.withId(R.id.vgsEtCity))
        .perform(ActionHelper.doAction<VGSEditText> {
            it.setText(city)
        })
    Espresso.onView(ViewMatchers.withId(R.id.vgsEtPostalAddress))
        .perform(ActionHelper.doAction<VGSEditText> {
            it.setText(postalAddress)
        })
}

internal inline fun <reified T : Parcelable> ActivityScenario<*>.getParcelableSafe(key: String): T? {
    val extras = safeResult.resultData?.extras
    extras?.classLoader = T::class.java.classLoader
    return extras?.getParcelable(key)
}