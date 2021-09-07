package com.verygoodsecurity.vgscheckout.util

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId

internal object ViewInteraction {

    fun onViewWithScrollTo(@IdRes id: Int): ViewInteraction = onView(withId(id)).perform(scrollTo())
}