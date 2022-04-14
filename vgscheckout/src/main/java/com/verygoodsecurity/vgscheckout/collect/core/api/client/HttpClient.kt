package com.verygoodsecurity.vgscheckout.collect.core.api.client

import com.verygoodsecurity.vgscheckout.collect.core.api.client.okhttp.OkHttpClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal interface HttpClient {

    fun setHost(url: String?)

    fun enqueue(request: HttpRequest, callback: ((NetworkResponse) -> Unit)? = null)

    fun execute(request: HttpRequest): NetworkResponse

    fun cancelAll()

    companion object Factory {

        fun create(
            printLogs: Boolean = true,
            executor: ExecutorService = Executors.newSingleThreadExecutor()
        ) = OkHttpClient(printLogs, executor)
    }
}