package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.action

internal abstract class Event {

    abstract fun getAttributes(): MutableMap<String, Any>
}