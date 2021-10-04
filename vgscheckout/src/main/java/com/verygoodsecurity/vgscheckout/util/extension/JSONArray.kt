package com.verygoodsecurity.vgscheckout.util.extension

import org.json.JSONArray

internal fun JSONArray.toStringList(): List<String>? {
    val result = mutableListOf<String>()
    (0 until this.length()).forEach {
        try {
            result.add(this.getString(it))
        } catch (e: Exception) {
            return null
        }
    }
    return result
}