package com.verygoodsecurity.vgscheckout.collect.core.api

internal interface ApiClientStorage {

    fun setCustomHeaders(headers: Map<String, String>)

    fun getCustomHeaders(): Map<String, String>

    fun resetCustomHeaders()

    fun setCustomData(data: Map<String, Any>)

    fun getCustomData(): Map<String, Any>

    fun resetCustomData()
}