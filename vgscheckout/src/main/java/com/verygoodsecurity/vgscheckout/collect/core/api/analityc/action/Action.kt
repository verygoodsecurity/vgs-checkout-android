package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

interface Action {
    fun getAttributes():MutableMap<String, Any>
}