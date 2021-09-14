package com.verygoodsecurity.vgscheckout.collect.core.api.analityc

import android.os.Build
import android.util.Log
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.VgsApiTemporaryStorageImpl
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action.Action
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toAnalyticRequest
import com.verygoodsecurity.vgscheckout.collect.util.extension.toIso8601
import java.util.*
import java.util.concurrent.Executors

internal class CollectActionTracker(
    val tnt: String,
    val environment: String,
    val formId: String
) : AnalyticTracker {

    override var isEnabled: Boolean = true

    internal object Sid {
        val id = "${UUID.randomUUID()}"
    }

    private val client: ApiClient by lazy {
        return@lazy ApiClient.newHttpClient(false, VgsApiTemporaryStorageImpl())
    }

    override fun logEvent(action: Action) {
        if (isEnabled) {
            val event = action.run {
                val sender = Event(client, tnt, environment, formId)
                sender.map = getAttributes()
                sender
            }

            Executors.newSingleThreadExecutor().submit(event)
        }
    }

    private class Event(
        private val client: ApiClient,
        private val tnt: String,
        private val environment: String,
        private val formId: String
    ) : Runnable {

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

        override fun run() {
            Log.d("Test", map.toString())
            val r = VGSRequest.VGSRequestBuilder()
                .setPath(ENDPOINT)
                .setMethod(HTTPMethod.POST)
                .setCustomData(map)
                .setFormat(VGSHttpBodyFormat.X_WWW_FORM_URLENCODED)
                .build()

            client.enqueue(r.toAnalyticRequest(URL))
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
        private const val ENDPOINT = "/vgs"
        private const val URL = "https://vgs-collect-keeper.apps.verygood.systems"
    }
}