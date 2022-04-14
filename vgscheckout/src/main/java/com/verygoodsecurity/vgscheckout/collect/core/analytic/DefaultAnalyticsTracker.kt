package com.verygoodsecurity.vgscheckout.collect.core.analytic

import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.collect.core.api.client.HttpClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpRequest
import com.verygoodsecurity.vgscheckout.collect.util.extension.toBase64
import com.verygoodsecurity.vgscheckout.collect.util.extension.toJSON
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class DefaultAnalyticsTracker @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) constructor(
    private val id: String,
    private val environment: String,
    private val formId: String,
    private val client: HttpClient
) : AnalyticTracker {

    override var isEnabled: Boolean = true

    constructor(id: String, environment: String, formId: String) : this(
        id,
        environment,
        formId,
        HttpClient.create(false, getExecutor())
    )

    override fun log(event: Event) {
        if (isEnabled.not()) {
            return
        }
        val payload = event.getData(id, formId, environment).toJSON().toString().toBase64()
        client.enqueue(
            HttpRequest(
                method = HTTPMethod.POST,
                url = API_URL,
                headers = emptyMap(),
                payload = payload,
                format = VGSHttpBodyFormat.X_WWW_FORM_URLENCODED,
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