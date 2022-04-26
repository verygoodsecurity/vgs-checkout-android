package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event

@Suppress("MemberVisibilityCanBePrivate")
internal class FinInstrumentCrudEvent private constructor(
    val code: Int,
    val isSuccessful: Boolean,
    val message: String?,
    val isCustomConfig: Boolean,
    val method: String,
    val totalCount: Int?,
    val failedCount: Int?,
) : Event(TYPE) {

    override val attributes: Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessful) STATUS_OK else STATUS_FAILED)
        put(KEY_STATUS_CODE, code)
        put(KEY_CONFIG, mapConfig(isCustomConfig))
        put(KEY_CONFIG_TYPE, CONFIG_TYPE)
        put(KEY_METHOD, method)
        message?.let { put(KEY_ERROR, it) }
        totalCount?.let { put(KEY_TOTAL_COUNT, it) }
        failedCount?.let { put(KEY_FAILED_COUNT, it) }
    }

    private fun mapConfig(isCustom: Boolean) = if (isCustom) CONFIG_CUSTOM else CONFIG_PAYOUT

    companion object {

        internal const val DEFAULT_CODE = 200

        private const val TYPE = "FinInstrument"

        private const val KEY_STATUS_CODE = "statusCode"
        private const val KEY_ERROR = "error"
        private const val KEY_CONFIG = "config"
        private const val KEY_CONFIG_TYPE = "configType"
        private const val KEY_METHOD = "method"
        private const val KEY_TOTAL_COUNT = "totalCount"
        private const val KEY_FAILED_COUNT = "failedCount"

        private const val CONFIG_CUSTOM = "custom"
        private const val CONFIG_PAYOUT = "payopt"
        private const val CONFIG_TYPE = "addCard"
        private const val CREATE_ACTION = "CreateFinInstrument"
        private const val DELETE_ACTION = "DeleteFinInstrument"
        private const val LOAD_ACTION = "LoadFinInstruments"

        fun create(code: Int, isSuccessful: Boolean, message: String?, isCustomConfig: Boolean) =
            FinInstrumentCrudEvent(
                code,
                isSuccessful,
                message,
                isCustomConfig,
                CREATE_ACTION,
                null,
                null
            )

        fun delete(code: Int, isSuccessful: Boolean, message: String?, isCustomConfig: Boolean) =
            FinInstrumentCrudEvent(
                code,
                isSuccessful,
                message,
                isCustomConfig,
                DELETE_ACTION,
                null,
                null
            )

        fun load(
            code: Int,
            isSuccessful: Boolean,
            message: String?,
            isCustomConfig: Boolean,
            totalCount: Int,
            failedCount: Int
        ) = FinInstrumentCrudEvent(
            code,
            isSuccessful,
            message,
            isCustomConfig,
            LOAD_ACTION,
            totalCount,
            failedCount
        )
    }
}