package com.verygoodsecurity.add.example.network

import com.google.gson.JsonParser
import com.verygoodsecurity.BuildConfig
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody

class HttpClient {

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private fun parseToken(body: String?) = body?.let {
            JsonParser
                .parseString(it)
                .asJsonObject
                .run {
                    takeIf { this.has(ACCESS_TOKEN) }
                        ?.run { get(ACCESS_TOKEN).asString } ?: ""
                }
        } ?: ""
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .build()
    }

    fun getAccessToken(callback: ((token: String?) -> Unit)?) {
        val okHttpRequest = Request.Builder()
            .method("POST", "{}".toRequestBody())
            .addHeader("Content-Type", "application/json")
            .url(BuildConfig.CLIENT_HOST + BuildConfig.GET_TOKEN_ENDPOINT)
            .build()

        try {
            client.newBuilder()
                .build()
                .newCall(okHttpRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback?.invoke(null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        parseToken(response.body?.string()).let {
                            callback?.invoke(it)
                        }
                    }
                })
        } catch (e: Exception) {
            callback?.invoke(null)
        }
    }
}