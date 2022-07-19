package com.verygoodsecurity.vgscheckout.analytic

import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpBodyFormat
import com.verygoodsecurity.vgscheckout.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.networking.client.HttpClient
import com.verygoodsecurity.vgscheckout.collect.util.extension.toBase64
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class DefaultAnalyticsTracker @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) constructor(
    private val id: String,
    private val environment: String,
    private val formId: String,
    private val routeId: String,
    private val client: HttpClient
) : AnalyticTracker {

    constructor(id: String, environment: String, formId: String, routeId: String) : this(
        id,
        environment,
        formId,
        routeId,
        HttpClient.create(false, getExecutor())
    )

    override fun log(event: Event) {
        if (VGSCheckoutAnalyticsLogger.isAnalyticsEnabled.not()) {
            return
        }
        val payload = event.getData(id, environment, formId, routeId).toJSON().toString().toBase64()
        client.enqueue(
            HttpRequest(
                url = API_URL,
                payload = payload,
                headers = emptyMap(),
                method = HttpMethod.POST,
                format = HttpBodyFormat.X_WWW_FORM_URLENCODED,
            )
        )
    }

    companion object {

        private const val API_URL = "https://vgs-collect-keeper.apps.verygood.systems/vgs"

        private fun getExecutor(): ExecutorService {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        }
    }
}