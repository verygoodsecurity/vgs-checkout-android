package com.verygoodsecurity.vgscheckout.api

import android.app.Activity
import com.verygoodsecurity.vgscheckout.TestApplication
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class SystemHeadersTest {

    private lateinit var activity: Activity
    private lateinit var collect: VGSCollect

    @Before
    fun setUp() {
        val activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()
        collect = VGSCollect(activity, "tnts")
    }

    @Test
    fun add_headers_during_init() {
        collect.updateAgentHeader()
        val headers:String? = collect.getClient().getStorage().getCustomHeaders()[AGENT]

        assertNotNull(headers)
        assertTrue(headers!!.contains(SOURCE_ANDROID))
        assertTrue(headers.contains(MEDIUM_CHECKOUT))
        assertTrue(headers.contains(CONTENT))
        assertTrue(headers.contains(OS))
        assertTrue(headers.contains(ID))
        assertTrue(headers.contains(TR))
    }

    companion object {

        private const val AGENT = "VGS-Client"
        private const val SOURCE_ANDROID = "source=checkout-android"
        private const val MEDIUM_CHECKOUT = "vgs-checkout"
        private const val CONTENT = "content"
        private const val OS = "osVersion"
        private const val ID = "vgsCheckoutSessionId"
        private const val TR = "tr"
    }
}