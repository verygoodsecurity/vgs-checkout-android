package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.util.extension.readExtraParcelable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

private const val VGS_SUCCESS_CODE = 200

@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun performCheckout_validParams_successfulResult() {
        // Arrange
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(
                "extra_checkout_config",
                VGSCheckoutConfiguration.Builder("tntpszqgikn")
                    .setRouteConfig(
                        VGSCheckoutRouteConfiguration.Builder()
                            .setPath("post")
                            .build()
                    )
                    .build()
            )
        }
        launch<CheckoutActivity>(intent).use {
            // Act
            onView(withId(R.id.mbPay)).perform(ViewActions.click())
            val result = getResultFixed(it)
            val vgsResultData =
                result.resultData?.readExtraParcelable<VGSCheckoutResult>(CHECKOUT_RESULT_EXTRA_KEY)
            //Assert
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals(VGS_SUCCESS_CODE, vgsResultData?.code)
        }
    }

    private fun <A : Activity> getResultFixed(rule: ActivityScenario<A>): Instrumentation.ActivityResult {
        fixActivityScenarioResult()
        return rule.result
    }

    /**
     * Used to fix activity scenario get result race condition.
     *
     * Read more here: https://github.com/android/android-test/issues/676.
     */
    private fun fixActivityScenarioResult() {
        Thread.sleep(3000)
    }
}