package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

internal interface Event {
    fun getAttributes():MutableMap<String, Any>
}