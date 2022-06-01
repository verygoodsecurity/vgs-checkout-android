package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.textfield.TextInputLayout
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import kotlin.reflect.KClass

internal object VGSViewMatchers {

    /**
     * Matcher that matches is VGSTextInputLayout show error message.
     */
    fun withError(error: String?) =
        object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(error)
            }

            override fun matchesSafely(item: TextInputLayout?): Boolean {
                return item?.error?.equals(error) == true
            }
        }

    /**
     * Matcher that matches VGSTextInputLayout hint.
     */
    fun withHint(hint: String?) =
        object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(hint)
            }

            override fun matchesSafely(item: TextInputLayout?): Boolean {
                return item?.hint?.toString().equals(hint)
            }
        }

    /**
     * Matcher that matches InputField text.
     */
    fun withText(text: String?) =
        object : BoundedMatcher<View, BaseInputField>(BaseInputField::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText(text)
            }

            override fun matchesSafely(item: BaseInputField?): Boolean {
                return item?.text?.toString().equals(text)
            }
        }

    /**
     * Matcher that matches InputField by it VGSTextInputLayout parent.
     */
    fun <T : View> withParent(@IdRes parentId: Int, klass: KClass<T>): Matcher<View> {
        return allOf(
            ViewMatchers.withClassName(Matchers.containsString(klass.simpleName)),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentId))
        )
    }
}