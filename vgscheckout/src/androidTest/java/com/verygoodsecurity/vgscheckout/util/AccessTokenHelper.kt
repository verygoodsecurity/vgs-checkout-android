package com.verygoodsecurity.vgscheckout.util

import com.google.gson.JsonParser
import com.verygoodsecurity.vgscheckout.BuildConfig
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody

class AccessTokenHelper {

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

    fun getToken(): String {
        return try {
            val okHttpRequest = Request.Builder()
                .method("POST", "{}".toRequestBody())
                //TODO replace to circleCI
                .url(BuildConfig.AUTHENTICATION_HOST)
                .build()

            client.newBuilder().build()
                .newCall(okHttpRequest)
                .execute().body?.string()
                ?.run {
                    parseToken(this)
                } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}