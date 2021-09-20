package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.Event
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toNetworkRequest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class DefaultAnalyticsTracker(
    private val vaultID: String,
    private val environment: String,
    private val formID: String
) : AnalyticTracker {

    override var isEnabled: Boolean = true

    private val client: ApiClient by lazy { ApiClient.create(false, getExecutor()) }

    override fun log(event: Event) {
        if (isEnabled.not()) {
            return
        }
        client.enqueue(
            VGSRequest.VGSRequestBuilder()
                .setPath(PATH)
                .setMethod(HTTPMethod.POST)
                .setCustomData(event.getPayload(vaultID, formID, environment))
                .setFormat(VGSHttpBodyFormat.X_WWW_FORM_URLENCODED)
                .build().toNetworkRequest(BASE_URL)
        )
    }

    private fun getExecutor(): ExecutorService {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    companion object {

        private const val BASE_URL = "https://vgs-collect-keeper.apps.verygood.systems"
        private const val PATH = "/vgs"
    }
}