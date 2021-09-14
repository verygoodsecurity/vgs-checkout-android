package com.verygoodsecurity.vgscheckout.collect.core.api

internal class DefaultApiClientStorage : ApiClientStorage {

    private val data = HashMap<String, Any>()

    private val headers = HashMap<String, String>()

    override fun setCustomHeaders(headers: Map<String, String>) {
        this.headers.putAll(headers)
    }

    override fun getCustomHeaders(): Map<String, String> = headers

    override fun resetCustomHeaders() {
        headers.clear()
    }

    override fun setCustomData(data: Map<String, Any>) {
        this.data.putAll(data)
    }

    override fun getCustomData(): Map<String, Any> = data

    override fun resetCustomData() {
        data.clear()
    }
}