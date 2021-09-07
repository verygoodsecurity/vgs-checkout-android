package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout
import org.hamcrest.Description

internal object VGSViewMatchers {

    /**
     * Matcher that matches is VGSTextInputLayout show error message.
     */
    fun withError(error: String?) =
        object : BoundedMatcher<View, VGSTextInputLayout>(VGSTextInputLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(error)
            }

            override fun matchesSafely(item: VGSTextInputLayout?): Boolean {
                return item?.getError().equals(error)
            }
        }

    /**
     * Matcher that matches VGSTextInputLayout hint.
     */
    fun withHint(hint: String?) =
        object : BoundedMatcher<View, VGSTextInputLayout>(VGSTextInputLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(hint)
            }

            override fun matchesSafely(item: VGSTextInputLayout?): Boolean {
                return item?.getHint()?.toString().equals(hint)
            }
        }

    /**
     * Matcher that matches InputField text.
     */
    fun withText(text: String?) =
        object : BoundedMatcher<View, InputFieldView>(InputFieldView::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(text)
            }

            override fun matchesSafely(item: InputFieldView?): Boolean {
                return item?.getText()?.toString().equals(text)
            }
        }
}