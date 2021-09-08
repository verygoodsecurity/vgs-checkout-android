package com.verygoodsecurity.vgscheckout.collect.core.api.client

import android.os.Build
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.api.VgsApiTemporaryStorage
import com.verygoodsecurity.vgscheckout.collect.core.api.VgsApiTemporaryStorageImpl
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.CollectActionTracker
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse

internal interface ApiClient {

    fun setHost(url: String?)

    fun enqueue(request: NetworkRequest, callback: ((NetworkResponse) -> Unit)? = null)
    fun execute(request: NetworkRequest): NetworkResponse
    fun cancelAll()

    fun getTemporaryStorage(): VgsApiTemporaryStorage

    //todo update or remvoe according to the analytic requirements
    companion object {
        internal const val CONTENT_TYPE = "Content-type"
        private const val AGENT = "vgs-client"
        private const val TEMPORARY_AGENT_TEMPLATE =
            "source=androidSDK&medium=vgs-collect&content=%s&vgsCollectSessionId=%s&tr=%s"

        fun newHttpClient(
            url: String,
            isLogsVisible: Boolean = true,
            storage: VgsApiTemporaryStorage? = null
        ): ApiClient {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                OkHttpClient(
                    isLogsVisible,
                    storage ?: VgsApiTemporaryStorageImpl()
                ).apply { setHost(url) }
            } else {
                URLConnectionClient(
                    isLogsVisible,
                    (storage ?: VgsApiTemporaryStorageImpl())
                ).apply {
                    setHost(url)
                }
            }
        }

        fun newHttpClient(
            isLogsVisible: Boolean = true,
            storage: VgsApiTemporaryStorage? = null
        ): ApiClient {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                OkHttpClient(isLogsVisible, storage ?: VgsApiTemporaryStorageImpl())
            } else {
                URLConnectionClient(
                    isLogsVisible,
                    (storage ?: VgsApiTemporaryStorageImpl())
                )
            }
        }

        fun generateAgentHeader(isAnalyticsEnabled: Boolean): Pair<String, String> =
            AGENT to String.format(
                TEMPORARY_AGENT_TEMPLATE,
                BuildConfig.VERSION_NAME,
                CollectActionTracker.Sid.id,
                if (isAnalyticsEnabled) "default" else "none"
            )
    }
}