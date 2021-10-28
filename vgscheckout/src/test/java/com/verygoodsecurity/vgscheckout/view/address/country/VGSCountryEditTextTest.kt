package com.verygoodsecurity.vgscheckout.view.address.country

import android.app.Activity
import com.verygoodsecurity.vgscheckout.TestApplication
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.internal.CountryInputField
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class VGSCountryEditTextTest {

    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity

    private lateinit var view: VGSCountryEditText

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()

        view = VGSCountryEditText(activity)
    }


    @Test
    fun test_view() {
        view.onAttachedToWindow()
        val internal = view.statePreparer.getView()
        Assert.assertNotNull(internal)
    }

    @Test
    fun test_attach_view() {
        view.onAttachedToWindow()

        Assert.assertEquals(1, view.childCount)
    }

    @Test
    fun test_check_internal_view() {
        val internal = view.statePreparer.getView()
        Assert.assertNotNull(internal)

        val child = view.statePreparer.getView()
        Assert.assertTrue(child is CountryInputField)
    }

    @Test
    fun test_type() {
        Assert.assertEquals(FieldType.COUNTRY, view.getFieldType())
    }
}