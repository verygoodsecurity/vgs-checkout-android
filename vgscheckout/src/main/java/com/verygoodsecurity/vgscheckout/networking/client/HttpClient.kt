package com.verygoodsecurity.vgscheckout.networking.client

import com.verygoodsecurity.vgscheckout.networking.client.okhttp.OkHttpClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal interface HttpClient {

    fun setHost(url: String?)

    fun enqueue(request: HttpRequest, callback: ((HttpResponse) -> Unit)? = null)

    fun execute(request: HttpRequest): HttpResponse

    fun cancelAll()

    companion object Factory {

        fun create(
            printLogs: Boolean = true,
            executor: ExecutorService = Executors.newSingleThreadExecutor()
        ) = OkHttpClient(printLogs, executor)
    }
}