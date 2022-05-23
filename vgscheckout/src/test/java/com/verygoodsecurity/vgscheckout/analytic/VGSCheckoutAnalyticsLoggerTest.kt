package com.verygoodsecurity.vgscheckout.analytic

import com.verygoodsecurity.vgscheckout.analytic.event.InitEvent
import com.verygoodsecurity.vgscheckout.analytic.event.core.ENVIRONMENT
import com.verygoodsecurity.vgscheckout.analytic.event.core.FORM_ID
import com.verygoodsecurity.vgscheckout.analytic.event.core.ID
import com.verygoodsecurity.vgscheckout.capture
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.client.okhttp.OkHttpClient
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class VGSCheckoutAnalyticsLoggerTest {

    private val mockHttpClient: HttpClient = Mockito.mock(OkHttpClient::class.java)
    private val callback: (HttpResponse) -> Unit = {}
    private var tracker = DefaultAnalyticsTracker(ID, ENVIRONMENT, FORM_ID, mockHttpClient)

    @Test
    fun log_analyticsEnabled_apiClientCalled() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, VGSCheckoutCustomConfig(ID))
        val httpRequestCaptor: ArgumentCaptor<HttpRequest> =
            ArgumentCaptor.forClass(HttpRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(HttpResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        Mockito.verify(mockHttpClient, Mockito.times(1)).enqueue(
            capture(httpRequestCaptor),
            capture(callbackCaptor)
        )
    }

    @Test
    fun log_analyticsDisabled_apiClientNotCalled() {
        // Arrange
        VGSCheckoutAnalyticsLogger.isAnalyticsEnabled = false
        val event = InitEvent(InitEvent.ConfigType.CUSTOM, VGSCheckoutCustomConfig(ID))
        val httpRequestCaptor: ArgumentCaptor<HttpRequest> =
            ArgumentCaptor.forClass(HttpRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(HttpResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        Mockito.verify(mockHttpClient, Mockito.never()).enqueue(
            capture(httpRequestCaptor),
            capture(callbackCaptor)
        )
    }
}