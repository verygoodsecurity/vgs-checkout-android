package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import com.verygoodsecurity.vgscheckout.capture
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.InitEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.client.HttpClient
import com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp.OkHttpClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import com.verygoodsecurity.vgscheckout.collect.util.extension.toBase64
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

private const val ID = "test_vault_id"
private const val FORM_ID = "test_form_id"
private const val ENVIRONMENT = "test_env"

class DefaultAnalyticsTrackerTest {

    private val mockHttpClient: HttpClient = mock(OkHttpClient::class.java)
    private val callback: (NetworkResponse) -> Unit = {}
    private var tracker = DefaultAnalyticsTracker(ID, ENVIRONMENT, FORM_ID, mockHttpClient)

    @Test
    fun log_analyticsEnabled_apiClientCalled() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM)
        val networkRequestCaptor: ArgumentCaptor<NetworkRequest> =
            ArgumentCaptor.forClass(NetworkRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(NetworkResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        verify(mockHttpClient, times(1)).enqueue(
            capture(networkRequestCaptor),
            capture(callbackCaptor)
        )
    }

    @Test
    fun log_analyticsDisabled_apiClientCalled() {
        // Arrange
        tracker.isEnabled = false
        val event = InitEvent(InitEvent.ConfigType.CUSTOM)
        val networkRequestCaptor: ArgumentCaptor<NetworkRequest> =
            ArgumentCaptor.forClass(NetworkRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(NetworkResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        verify(mockHttpClient, never()).enqueue(
            capture(networkRequestCaptor),
            capture(callbackCaptor)
        )
    }

    @Test
    fun log_apiCalledWithCorrectData() {
        // Arrange
        val event = InitEvent(InitEvent.ConfigType.CUSTOM)
        val payload = event.getData(ID, FORM_ID, ENVIRONMENT).toJSON().toString().toBase64()
        val expectedNetworkRequest = NetworkRequest(
            method = HTTPMethod.POST,
            url = "https://vgs-collect-keeper.apps.verygood.systems/vgs",
            customHeader = emptyMap(),
            customData = payload,
            fieldsIgnore = false,
            fileIgnore = false,
            format = VGSHttpBodyFormat.X_WWW_FORM_URLENCODED,
            requestTimeoutInterval = 60_000L
        )
        val networkRequestCaptor: ArgumentCaptor<NetworkRequest> =
            ArgumentCaptor.forClass(NetworkRequest::class.java)
        val callbackCaptor: ArgumentCaptor<(NetworkResponse) -> Unit> =
            ArgumentCaptor.forClass(callback::class.java)
        // Act
        tracker.log(event)
        // Assert
        verify(mockHttpClient, times(1)).enqueue(
            capture(networkRequestCaptor),
            capture(callbackCaptor)
        )
        assertEquals(expectedNetworkRequest, networkRequestCaptor.value)
    }
}