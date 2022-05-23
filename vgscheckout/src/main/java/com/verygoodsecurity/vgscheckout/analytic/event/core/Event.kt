package com.verygoodsecurity.vgscheckout.analytic.event.core

import android.os.Build
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.util.Session
import java.text.SimpleDateFormat
import java.util.*

internal abstract class Event constructor(type: String, config: CheckoutConfig? = null) {

    protected abstract val attributes: Map<String, Any>

    private val data: MutableMap<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_TYPE, type)
        put(KEY_TIMESTAMP, System.currentTimeMillis())
        put(KEY_CLIENT_TIMESTAMP, getClientTime())
        put(KEY_SESSION_ID, Session.id)
        put(KEY_SOURCE, SOURCE)
        put(KEY_VERSION, BuildConfig.VERSION_NAME)
        put(KEY_STATUS, STATUS_OK)
        put(
            KEY_USER_AGENT, mapOf<String, Any>(
                KEY_PLATFORM to PLATFORM,
                KEY_DEVICE to Build.BRAND,
                KEY_DEVICE_MODEL to Build.MODEL,
                KEY_OS to Build.VERSION.SDK_INT.toString()
            )
        )
        config?.let { put(KEY_CONTENT, generateContent(it)) }
    }
        get() = field + attributes

    fun getData(id: String, formId: String, environment: String): Map<String, Any> {
        return data.apply {
            put(KEY_ID, id)
            put(KEY_FORM_ID, formId)
            put(KEY_ENVIRONMENT, environment)
        }
    }

    private fun generateContent(config: CheckoutConfig): List<String> {
        return mutableListOf<String>().apply {
            with(config) {
                add(mapValidationBehaviour(formConfig.validationBehaviour))
                add(mapBillingAddressVisibility(formConfig.addressOptions.visibility))
                add(routeConfig.requestOptions.mergePolicy.name.lowercase())
                if (routeConfig.hostnamePolicy is VGSCheckoutHostnamePolicy.CustomHostname) {
                    add(CUSTOM_HOSTNAME)
                }
                if (routeConfig.requestOptions.hasExtraHeaders) {
                    add(CUSTOM_HEADER)
                }
                if (routeConfig.requestOptions.extraData.isNotEmpty()) {
                    add(CUSTOM_DATA)
                }
                if (formConfig.addressOptions.countryOptions.validCountries.isNotEmpty()) {
                    add(VALID_COUNTRIES)
                }
                if (this@with is VGSCheckoutAddCardConfig && formConfig.saveCardOptionEnabled) {
                    add(SAVE_CARD_CHECKBOX_ENABLED)
                }
            }
        }
    }

    private fun mapValidationBehaviour(behaviour: VGSCheckoutFormValidationBehaviour) =
        when (behaviour) {
            VGSCheckoutFormValidationBehaviour.ON_FOCUS -> ON_FOCUS_VALIDATION
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT -> ON_SUBMIT_VALIDATION
        }

    private fun mapBillingAddressVisibility(visibility: VGSCheckoutBillingAddressVisibility) =
        when (visibility) {
            VGSCheckoutBillingAddressVisibility.VISIBLE -> BILLING_ADDRESS_VISIBLE
            VGSCheckoutBillingAddressVisibility.HIDDEN -> BILLING_ADDRESS_HIDDEN
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
        private const val KEY_TIMESTAMP = "localTimestamp"
        private const val KEY_CLIENT_TIMESTAMP = "clientTimestamp"
        private const val KEY_SESSION_ID = "vgsCheckoutSessionId"
        private const val KEY_SOURCE = "source"
        private const val KEY_VERSION = "version"
        private const val KEY_USER_AGENT = "ua"
        private const val KEY_PLATFORM = "source"
        private const val KEY_DEVICE = "device"
        private const val KEY_DEVICE_MODEL = "deviceModel"
        private const val KEY_OS = "osVersion"
        private const val KEY_ID = "tnt"
        private const val KEY_FORM_ID = "formId"
        private const val KEY_ENVIRONMENT = "env"
        private const val KEY_CONTENT = "content"

        private const val SOURCE = "checkout-android"
        private const val PLATFORM = "android"
        internal const val STATUS_OK = "Ok"
        internal const val STATUS_FAILED = "Failed"

        private const val CUSTOM_HOSTNAME = "custom_hostname"
        private const val CUSTOM_DATA = "custom_data"
        private const val CUSTOM_HEADER = "custom_header"
        private const val VALID_COUNTRIES = "valid_countries"
        private const val ON_SUBMIT_VALIDATION = "on_submit_validation"
        private const val ON_FOCUS_VALIDATION = "on_focus_validation"
        private const val BILLING_ADDRESS_VISIBLE = "billing_address_visible"
        private const val BILLING_ADDRESS_HIDDEN = "billing_address_hidden"
        private const val SAVE_CARD_CHECKBOX_ENABLED = "save_card_checkbox"
    }
}