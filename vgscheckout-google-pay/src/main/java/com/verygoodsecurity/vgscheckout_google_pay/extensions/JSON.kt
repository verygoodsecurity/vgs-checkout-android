package com.verygoodsecurity.vgscheckout_google_pay.extensions

import org.json.JSONArray

fun JSONArray.toStringList(): List<String> {
    val result = mutableListOf<String>()
    for (i in 0 until length()) {
        result.add(get(i).toString())
    }
    return result
}