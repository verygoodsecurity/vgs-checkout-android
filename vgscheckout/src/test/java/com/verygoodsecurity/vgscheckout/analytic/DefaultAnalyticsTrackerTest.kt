package com.verygoodsecurity.vgscheckout.analytic

import com.verygoodsecurity.vgscheckout.capture
import com.verygoodsecurity.vgscheckout.analytic.event.InitEvent
import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.networking.client.okhttp.OkHttpClient
import com.verygoodsecurity.vgscheckout.collect.util.extension.toBase64
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.networking.client.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class DefaultAnalyticsTrackerTest {

    private val mockHttpClient: HttpClient = mock(OkHttpClient::class.java)
    private val callback: (HttpResponse) -> Unit = {}

    @Before
    fun setup() {
        VGSCheckoutAnalyticsLogger.isAnalyticsEnabled = true
    }

    @Test
    fun constructor_analyticTrackerCreated() {
        // Act
        val tracker = DefaultAnalyticsTracker(ID, ENVIRONMENT, FORM_ID)
        // Assert
        assertNotNull(tracker)
    }

    @Test
    fun log_apiCalledWithCorrectData() {
        // Arrange
        val config = VGSCheckoutCustomConfig.Builder(ID).build()
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, config)
        val payload = event.getData(ID, FORM_ID, ENVIRONMENT).toJSON().toString().toBase64()

        val tracker = DefaultAnalyticsTracker(ID, ENVIRONMENT, FORM_ID, mockHttpClient)
        val expectedNetworkRequest = HttpRequest(
            method = HttpMethod.POST,
            url = "https://vgs-collect-keeper.apps.verygood.systems/vgs",
            headers = emptyMap(),
            payload = payload,
            format = HttpBodyFormat.X_WWW_FORM_URLENCODED,
            timeoutInterval = 60_000L
        )
        val httpRequestCaptor: ArgumentCaptor<HttpRequest> =
            ArgumentCaptor.forClass(HttpRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(HttpResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        verify(mockHttpClient, times(1)).enqueue(
            capture(httpRequestCaptor),
            capture(callbackCaptor)
        )
        assertEquals(expectedNetworkRequest, httpRequestCaptor.value)
    }
}