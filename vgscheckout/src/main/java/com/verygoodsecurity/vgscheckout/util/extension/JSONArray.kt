package com.verygoodsecurity.vgscheckout.util.extension

import org.json.JSONArray

internal fun <V> JSONArray.contains(value: V): Boolean {
    (0..this.length()).forEach {
        if (this.get(it) == value) {
            return true
        }
    }
    return false
}