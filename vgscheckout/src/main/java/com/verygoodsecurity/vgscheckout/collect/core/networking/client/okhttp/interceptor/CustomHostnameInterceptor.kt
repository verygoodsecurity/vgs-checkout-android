package com.verygoodsecurity.vgscheckout.collect.core.networking.client.okhttp.interceptor

import okhttp3.Interceptor
import okhttp3.Response

internal class CustomHostnameInterceptor : Interceptor {
    var host: String? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        val r = with(chain.request()) {
            if (!host.isNullOrBlank() && host != url.host) {
                val newUrl = chain.request().url.newBuilder()
                    .scheme(url.scheme)
                    .host(host!!)
                    .build()

                chain.request().newBuilder()
                    .url(newUrl)
                    .build()
            } else {
                this
            }
        }

        return chain.proceed(r)
    }
}