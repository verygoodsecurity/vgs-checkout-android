package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.collect.core.Environment
import com.verygoodsecurity.vgscheckout.config.ROUTE_ID_VALUE
import com.verygoodsecurity.vgscheckout.networking.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.Pattern

class UrlExtensionTest {
    companion object {
        private const val URL_REGEX =
            "^(https:\\/\\/)+[a-zA-Z0-9]+[.]+((live|sandbox|LIVE|SANDBOX)+((-)+([a-zA-Z0-9]+)|)+)+[.](verygoodproxy.com)\$"
    }

    @Test
    fun test_is_tennantId_not_valid() {

        val testUrl1 = " "
        assertFalse(testUrl1.isTenantIdValid())

        val testUrl2 = "tnt.com"
        assertFalse(testUrl2.isTenantIdValid())

        val testUrl3 = "tnt com"
        assertFalse(testUrl3.isTenantIdValid())

        val testUrl4 = "2tnt/com"
        assertFalse(testUrl4.isTenantIdValid())

        val testUrl5 = "tnt:com"
        assertFalse(testUrl5.isTenantIdValid())

        val testUrl6 = "tnt*com"
        assertFalse(testUrl6.isTenantIdValid())

        val testUrl7 = "tnt?com"
        assertFalse(testUrl7.isTenantIdValid())
    }

    @Test
    fun test_is_tennantId_valid() {
        val testUrl1 = "tntdldf"
        assertTrue(testUrl1.isTenantIdValid())
        val testUrl = "123"
        assertTrue(testUrl.isTenantIdValid())
    }

    @Test
    fun test_is_URL_valid() {
        val url1 = "google.com"
        assertTrue(url1.isUrlValid())

        val url2 = "https://www.bla"
        assertTrue(url2.isUrlValid())

        val url3 = "https://www.bla-one.com"
        assertTrue(url3.isUrlValid())

        val url4 = "https://www.bla-one.com/post"
        assertTrue(url4.isUrlValid())
    }

    @Test
    fun test_is_url_not_valid() {
        val url1 = "htt://www.bla"
        assertFalse(url1.isUrlValid())

        val url2 = "http:/www.bla"
        assertFalse(url2.isUrlValid())

        val url3 = "http://www.ex ample.com:8800"
        assertFalse(url3.isUrlValid())
    }

    @Test
    fun test_environment_sandbox_by_default() {
        val url = "abc".setupURL(Environment.SANDBOX.rawValue)
        assertTrue(url.contains("sandbox"))
    }

    @Test
    fun test_environment_live_by_default() {
        val url = "ab2c".setupURL(Environment.LIVE.rawValue)
        assertTrue(url.contains("live"))
    }

    @Test
    fun test_url_sandbox() {
        val s = "tnt234mm"
        val url = s.setupURL(Environment.SANDBOX.rawValue)

        assertTrue(Pattern.compile(URL_REGEX).matcher(url).matches())
    }

    @Test
    fun test_url_live() {
        val s = "1239f3hf"
        val url = s.setupURL(Environment.LIVE.rawValue)

        assertTrue(Pattern.compile(URL_REGEX).matcher(url).matches())
    }

    @Test
    fun test_setup_url_sandbox() {
        val s = "tnt234mm"
        val url = s.setupURL(Environment.SANDBOX.rawValue)

        assertTrue(url.isUrlValid())
    }

    @Test
    fun test_setup_url_live() {
        val s = "123456"
        val url = s.setupURL(Environment.LIVE.rawValue)

        assertTrue(url.isUrlValid())
    }

    @Test
    fun test_setup_url_payment() {
        val s = "tnt234mm"
        val defaultUrl = s.setupURL(Environment.LIVE.rawValue)
        assertFalse(defaultUrl.contains(ROUTE_ID_VALUE))

        val paymentUrl =
            s.setupURL(Environment.LIVE.rawValue, ROUTE_ID_VALUE)
        assertTrue(paymentUrl.contains(ROUTE_ID_VALUE))
    }

    @Test
    fun test_is_environment_valid() {
        assertTrue("live".isEnvironmentValid())
        assertFalse("live-".isEnvironmentValid())
        assertTrue("live-12".isEnvironmentValid())
        assertTrue("live-eu".isEnvironmentValid())
        assertTrue("live-w".isEnvironmentValid())
        assertFalse("live-w-".isEnvironmentValid())
        assertTrue("live-w-12".isEnvironmentValid())
        assertFalse("live-w-12-".isEnvironmentValid())
        assertTrue("live-w-12-abba".isEnvironmentValid())
        assertTrue("LIVE-UA-3".isEnvironmentValid())

        assertTrue("sandbox".isEnvironmentValid())
        assertFalse("sandbox-".isEnvironmentValid())
        assertTrue("sandbox-12".isEnvironmentValid())
        assertTrue("sandbox-eu".isEnvironmentValid())
        assertTrue("sandbox-w".isEnvironmentValid())
        assertFalse("sandbox-w-".isEnvironmentValid())
        assertTrue("sandbox-w-12".isEnvironmentValid())
        assertFalse("sandbox-w-12-".isEnvironmentValid())
        assertTrue("sandbox-w-12-abba".isEnvironmentValid())
        assertTrue("SANDBOX-UA-7".isEnvironmentValid())
    }

    @Test
    fun test_full_live_url() {
        val tennant = "acv12das"
        val url0 = tennant.setupURL("live")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url0).matches())

        val url1 = tennant.setupURL("live-")
        assertFalse(Pattern.compile(URL_REGEX).matcher(url1).matches())

        val url2 = tennant.setupURL("live-eu")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url2).matches())

        val url3 = tennant.setupURL("live-eu-")
        assertFalse(Pattern.compile(URL_REGEX).matcher(url3).matches())

        val url4 = tennant.setupURL("live-eu-123")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url4).matches())

        val url5 = tennant.setupURL("LIVE-UA-1")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url5).matches())
    }

    @Test
    fun test_full_sandbox_url() {
        val tennant = "acv12das"
        val url0 = tennant.setupURL("sandbox")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url0).matches())

        val url1 = tennant.setupURL("sandbox-")
        assertFalse(Pattern.compile(URL_REGEX).matcher(url1).matches())

        val url2 = tennant.setupURL("sandbox-eu")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url2).matches())

        val url3 = tennant.setupURL("sandbox-eu-")
        assertFalse(Pattern.compile(URL_REGEX).matcher(url3).matches())

        val url4 = tennant.setupURL("sandbox-eu-123")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url4).matches())

        val url5 = tennant.setupURL("SANDBOX-EU-1")
        assertTrue(Pattern.compile(URL_REGEX).matcher(url5).matches())
    }
}