package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.core

import android.os.Build
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.util.extension.toIso8601

internal abstract class Event constructor(type: String, params: Map<String, Any>) {

    private val data: MutableMap<String, Any> = mutableMapOf(
        KEY_TYPE to type,
        KEY_TIMESTAMP to System.currentTimeMillis(),
        KEY_CLIENT_TIMESTAMP to System.currentTimeMillis().toIso8601(),
        KEY_SOURCE to SOURCE,
        KEY_VERSION to BuildConfig.VERSION_NAME,
        KEY_STATUS to DEFAULT_STATUS,
        KEY_USER_AGENT to mapOf<String, Any>(
            KEY_PLATFORM to PLATFORM,
            KEY_DEVICE to Build.BRAND,
            KEY_DEVICE_MODEL to Build.MODEL,
            KEY_OS to Build.VERSION.SDK_INT.toString()
        )
    ).also { it.putAll(params) }

    fun getData(vaultID: String, formID: String, environment: String): Map<String, Any> {
        return data.apply {
            put(KEY_VAULT_ID, vaultID)
            put(KEY_FORM_ID, formID)
            put(KEY_ENVIRONMENT, environment)
        }
    }

    companion object {

        private const val KEY_TYPE = "type"
        private const val KEY_TIMESTAMP = "localTimestamp"
        private const val KEY_CLIENT_TIMESTAMP = "clientTimestamp"
        private const val KEY_SOURCE = "source"
        private const val KEY_VERSION = "version"
        private const val KEY_STATUS = "status"
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
        private const val DEFAULT_STATUS = "Ok"
    }
}