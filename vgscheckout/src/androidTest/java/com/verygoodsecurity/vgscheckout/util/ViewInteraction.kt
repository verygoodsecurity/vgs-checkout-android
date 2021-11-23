package com.verygoodsecurity.vgscheckout.util

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher

internal object ViewInteraction {

    fun onViewWithScrollTo(@IdRes id: Int): ViewInteraction = onView(withId(id)).perform(scrollTo())

    fun onViewWithScrollTo(viewMather: Matcher<View>): ViewInteraction =
        onView(viewMather).perform(scrollTo())
}