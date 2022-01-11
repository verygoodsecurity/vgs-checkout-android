package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core.Event
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.util.extension.toBase64
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class DefaultAnalyticsTracker @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) constructor(
    private val id: String,
    private val environment: String,
    private val formId: String,
    private val client: ApiClient
) : AnalyticTracker {

    override var isEnabled: Boolean = true

    constructor(id: String, environment: String, formId: String) : this(
        id,
        environment,
        formId,
        ApiClient.create(false, getExecutor())
    )

    override fun log(event: Event) {
        if (isEnabled.not()) {
            return
        }
        val payload = event.getData(id, formId, environment).toJSON().toString().toBase64()
        client.enqueue(
            NetworkRequest(
                method = HTTPMethod.POST,
                url = API_URL,
                customHeader = emptyMap(),
                customData = payload,
                fieldsIgnore = false,
                fileIgnore = false,
                format = VGSHttpBodyFormat.X_WWW_FORM_URLENCODED,
                requestTimeoutInterval = ANALYTICS_REQUEST_TIMEOUT
            )
        )
    }

    companion object {

        private const val API_URL = "https://vgs-collect-keeper.apps.verygood.systems/vgs"

        private const val ANALYTICS_REQUEST_TIMEOUT = 60_000L

        private fun getExecutor(): ExecutorService {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        }
    }
}