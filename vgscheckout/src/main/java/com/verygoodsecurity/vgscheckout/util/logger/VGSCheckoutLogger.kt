package com.verygoodsecurity.vgscheckout.util.logger

import android.util.Log
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger.Level.*

/**
 * This object is used to log messages in VGS Collect SDK.
 */
object VGSCheckoutLogger {

    const val TAG = "VGSCheckout"

    /** Current priority level for filtering debugging logs */
    var logLevel: Level = DEBUG

    /** Allows enable and disable debug-log printing. */
    var isEnabled = true

    /** Current logs tag */
    var tag: String = TAG

    /**
     * Priority constant for the printing debug-logs.
     */
    enum class Level {

        /**
         * Default setting. We print all information about processing.
         * It includes errors, warnings, notifications, debug messages, requests and responses.
         */
        DEBUG,

        /**
         * This setting allows you to minimize information and print only errors, warnings.
         */
        WARN,

        /**
         * Disable debug-logs.
         */
        NONE
    }

    /**
     * Returns true if the logger print log messages.
     */
    @Suppress("unused")
    fun isDebugEnabled(): Boolean = isEnabled && (logLevel.ordinal != NONE.ordinal)

    private fun printLog(level: Level, prefix: String?, message: String) {
        if (isEnabled && level.ordinal >= logLevel.ordinal) {
            val log: String = if (prefix.isNullOrEmpty()) message else "$prefix: $message"

            when (level) {
                DEBUG -> Log.d(tag, log)
                WARN -> Log.w(tag, log)
                NONE -> Unit
            }
        }
    }

    /**
     * Print a WARN log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     */
    internal fun warn(tag: String? = null, message: String) {
        printLog(WARN, tag, message)
    }

    /**
     * Print a WARN log message due to exception.
     *
     * @param tag Used to identify the source of a log message.
     * @param e exception.
     */
    internal fun warn(tag: String? = null, e: Exception) {
        warn(tag, "e: ${e::class.java}, message: ${e.message}")
    }

    /**
     * Print a WARN log message due to exception.
     *
     * @param tag Used to identify the source of a log message.
     * @param e exception.
     */
    internal fun warn(tag: String? = null, e: VGSCheckoutException) {
        warn(tag, "e: ${e::class.java}, code = ${e.code}, message: ${e.message}")
    }

    /**
     * Print a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     */
    internal fun debug(tag: String? = null, message: String) {
        printLog(DEBUG, tag, message)
    }
}