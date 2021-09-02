package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout
import org.hamcrest.Description

internal object Matchers {

    /**
     * Matcher tht matches is VGSTextInputLayout show error message.
     */
    fun error(error: String?) =
        object : BoundedMatcher<View, VGSTextInputLayout>(VGSTextInputLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(error)
            }

            override fun matchesSafely(item: VGSTextInputLayout?): Boolean {
                return item?.getError().equals(error)
            }

            override fun describeMismatch(item: Any?, description: Description?) {
                description
                super.describeMismatch(item, description)
            }
        }
}