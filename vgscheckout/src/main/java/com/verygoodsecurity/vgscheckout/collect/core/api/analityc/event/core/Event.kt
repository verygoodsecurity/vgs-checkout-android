package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core

import android.os.Build
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.util.Session
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

internal abstract class Event constructor(type: String) {

    protected abstract val attributes: Map<String, Any>

    private val data: MutableMap<String, Any> = mutableMapOf(
        KEY_TYPE to type,
        KEY_CLIENT_TIMESTAMP to getClientTime(),
        KEY_SESSION_ID to Session.id,
        KEY_SOURCE to SOURCE,
        KEY_VERSION to BuildConfig.VERSION_NAME,
        KEY_STATUS to STATUS_OK,
        KEY_USER_AGENT to mapOf<String, Any>(
            KEY_PLATFORM to PLATFORM,
            KEY_DEVICE to Build.BRAND,
            KEY_DEVICE_MODEL to Build.MODEL,
            KEY_OS to Build.VERSION.SDK_INT.toString()
        )
    )
        get() = field + attributes

    fun getData(vaultID: String, formID: String, environment: String): Map<String, Any> {
        return data.apply {
            put(KEY_VAULT_ID, vaultID)
            put(KEY_FORM_ID, formID)
            put(KEY_ENVIRONMENT, environment)
        }
    }

    private fun getClientTime(): String {
        return SimpleDateFormat(ISO_8601, Locale.US).format(System.currentTimeMillis())
    }

    private operator fun <K, V> Map<out K, V>.plus(map: Map<out K, V>): MutableMap<K, V> =
        LinkedHashMap(this).apply { putAll(map) }

    companion object {

        private const val ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        internal const val KEY_STATUS = "status"
        private const val KEY_TYPE = "type"
        private const val KEY_CLIENT_TIMESTAMP = "clientTimestamp"
        private const val KEY_SESSION_ID = "vgsCheckoutSessionId"
        private const val KEY_SOURCE = "source"
        private const val KEY_VERSION = "version"
        private const val KEY_USER_AGENT = "ua"
        private const val KEY_PLATFORM = "source"
        private const val KEY_DEVICE = "device"
        private const val KEY_DEVICE_MODEL = "deviceModel"
        private const val KEY_OS = "osVersion"
        private const val KEY_VAULT_ID = "tnt"
        private const val KEY_FORM_ID = "formId"
        private const val KEY_ENVIRONMENT = "env"

        private const val SOURCE = "checkout-android"
        private const val PLATFORM = "android"
        internal const val STATUS_OK = "Ok"
        internal const val STATUS_FAILED = "Failed"
    }
}