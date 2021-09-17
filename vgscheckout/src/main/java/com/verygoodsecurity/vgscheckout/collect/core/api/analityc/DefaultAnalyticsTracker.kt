package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import android.os.Build
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.Event
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toNetworkRequest
import com.verygoodsecurity.vgscheckout.collect.util.extension.toIso8601
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
        val evenWrapper = EventWrapper(vaultID, environment, formID)
        evenWrapper.map = event.payload.toMutableMap()
        client.enqueue(
            VGSRequest.VGSRequestBuilder()
                .setPath(PATH)
                .setMethod(HTTPMethod.POST)
                .setCustomData(evenWrapper.map)
                .setFormat(VGSHttpBodyFormat.X_WWW_FORM_URLENCODED)
                .build().toNetworkRequest(BASE_URL)
        )
    }

    private fun getExecutor(): ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    private class EventWrapper(
        private val tnt: String,
        private val environment: String,
        private val formId: String
    ) {

        var map: MutableMap<String, Any> = mutableMapOf()
            set(value) {
                field = value
                field.putAll(attachDefaultInfo(value))
            }

        private fun attachDefaultInfo(map: MutableMap<String, Any>): Map<String, Any> {
            val timestamp = System.currentTimeMillis()
            return with(map) {
                this[FORM_ID] = formId
                this[SOURCE] = SOURCE_TAG
                this[TIMESTAMP] = timestamp
                this[CLIENT_TIMESTAMP] = timestamp.toIso8601()
                this[TNT] = tnt
                this[ENVIRONMENT] = environment
                this[VERSION] = BuildConfig.VERSION_NAME
                if (!this.containsKey(STATUS)) {
                    this[STATUS] = STATUS_OK
                }

                val deviceInfo = mutableMapOf<String, String>()
                deviceInfo[PLATFORM] = PLATFORM_TAG
                deviceInfo[DEVICE] = Build.BRAND
                deviceInfo[DEVICE_MODEL] = Build.MODEL
                deviceInfo[OS] = Build.VERSION.SDK_INT.toString()
                this[USER_AGENT] = deviceInfo

                this
            }
        }

        companion object {
            private const val FORM_ID = "formId"
            private const val TIMESTAMP = "localTimestamp"
            private const val CLIENT_TIMESTAMP = "clientTimestamp"
            private const val TNT = "tnt"
            private const val ENVIRONMENT = "env"
            private const val VERSION = "version"
            private const val PLATFORM = "source"
            private const val SOURCE = "source"
            private const val PLATFORM_TAG = "android"
            private const val SOURCE_TAG = "checkout-android"
            private const val DEVICE = "device"
            private const val DEVICE_MODEL = "deviceModel"
            private const val OS = "osVersion"
            private const val STATUS = "status"
            private const val STATUS_OK = "Ok"
            private const val USER_AGENT = "ua"
        }
    }

    companion object {


        private const val BASE_URL = "https://vgs-collect-keeper.apps.verygood.systems"
        private const val PATH = "/vgs"
    }
}