package com.verygoodsecurity.vgscheckout.api

import com.verygoodsecurity.vgscheckout.collect.core.*
import com.verygoodsecurity.vgscheckout.collect.core.api.HttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.client.HttpClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.HttpRequest
import org.junit.Test
import org.mockito.Mockito

class HttpClientTest {

    @Test
    fun test_api_execute() {
        val client = Mockito.mock(HttpClient::class.java)

        val headers = HashMap<String, String>()
        headers.put("NEW-HEADER", "header")
        val data = HashMap<String, Any>()
        data.put("customData", "dataset")

        val r = HttpRequest(
            HttpMethod.POST,
            "https://www.test.com/post",
            headers,
            data,
            HttpBodyFormat.JSON,
            60000L
        )

        client.execute(r)

        Mockito.verify(client).execute(r)
    }

    @Test
    fun test_api_enqueue() {
        val client = Mockito.mock(HttpClient::class.java)

        val headers = HashMap<String, String>()
        headers.put("NEW-HEADER", "header")
        val data = HashMap<String, Any>()
        data.put("customData", "dataset")

        val r = HttpRequest(
            HttpMethod.POST,
            "https://www.test.com/post",
            headers,
            data,
            HttpBodyFormat.JSON,
            60000L
        )

        client.enqueue(r)

        Mockito.verify(client).enqueue(r)
    }
}