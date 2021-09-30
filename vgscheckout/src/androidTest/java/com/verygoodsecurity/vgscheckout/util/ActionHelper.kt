package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

internal object ActionHelper {

    private const val DESCRIPTION = "doAction"

    inline fun <reified P : View> doAction(
        description: String = DESCRIPTION,
        crossinline action: (view: P) -> Unit
    ) = object : ViewAction {

        override fun getDescription(): String = description

        override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(P::class.java)

        override fun perform(uiController: UiController?, view: View?) {
            if (view == null || view !is P) {
                throw RuntimeException("View is null or can't be casted to ${P::class.java.simpleName}")
            }
            action.invoke(view)
        }
    }
}