package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

internal interface Action {
    fun getAttributes():MutableMap<String, Any>
}