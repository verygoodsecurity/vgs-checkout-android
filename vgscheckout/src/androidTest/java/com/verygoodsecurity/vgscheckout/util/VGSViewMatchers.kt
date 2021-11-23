package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout
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