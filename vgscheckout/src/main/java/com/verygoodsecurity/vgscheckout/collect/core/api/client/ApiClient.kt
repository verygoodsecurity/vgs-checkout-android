package com.verygoodsecurity.vgscheckout.collect.core.api.client

import com.verygoodsecurity.vgscheckout.collect.core.api.ApiClientStorage
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal interface ApiClient {

    fun setHost(url: String?)

    fun enqueue(request: NetworkRequest, callback: ((NetworkResponse) -> Unit)? = null)

    fun execute(request: NetworkRequest): NetworkResponse

    fun cancelAll()

    fun getStorage(): ApiClientStorage

    companion object Factory {

        fun create(
            printLogs: Boolean = true,
            executor: ExecutorService = Executors.newSingleThreadExecutor()
        ) = OkHttpClient(printLogs, executor)
    }
}