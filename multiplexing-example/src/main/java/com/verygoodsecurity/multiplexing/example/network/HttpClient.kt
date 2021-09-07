package com.verygoodsecurity.multiplexing.example.network

import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody

class HttpClient {

    private val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .build()
    }

    private fun buildRequest(url: String, body: String): Request {
        return Request.Builder()
            .method("POST", body.toRequestBody())
            .addHeader("Content-Type", "application/json")
            .url(url)
            .build()
    }

    fun enqueue(url: String, body: String, callback: ((code: Int, body: String?) -> Unit)?) {
        val okHttpRequest = buildRequest(url, body)

        try {
            client.newBuilder()
                .build()
                .newCall(okHttpRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback?.invoke(-1, null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        callback?.invoke(
                            response.code,
                            response.body?.string()
                        )
                    }
                })
        } catch (e: Exception) {
            callback?.invoke(-1, null)
        }
    }
}