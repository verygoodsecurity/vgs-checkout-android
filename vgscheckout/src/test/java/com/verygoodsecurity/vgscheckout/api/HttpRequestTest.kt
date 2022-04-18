package com.verygoodsecurity.vgscheckout.api

import com.verygoodsecurity.vgscheckout.collect.core.HttpMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.HttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.model.network.*
import com.verygoodsecurity.vgscheckout.util.extension.toVGSResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class HttpRequestTest {

    @Test
    fun test_create_default_request() {
        val r = VGSRequest.VGSRequestBuilder().build()
        assertEquals("", r.path)
        assertEquals(HttpMethod.POST, r.method)
        assertEquals(HashMap<String, String>(), r.customData)
        assertEquals(HashMap<String, String>(), r.customHeader)
    }

    @Test
    fun test_create_request_with_custom_data() {
        val METHOD = HttpMethod.POST
        val PATH = "/some/path"
        val headers = HashMap<String, String>()
        headers["HEADER-S"] = "some-data"
        val data = HashMap<String, String>()
        data["customdata"] = "some-data2"

        val r = VGSRequest.VGSRequestBuilder()
            .setMethod(METHOD)
            .setPath(PATH)
            .setCustomData(data)
            .setCustomHeader(headers)
            .build()

        assertEquals(PATH, r.path)
        assertEquals(METHOD, r.method)
        assertEquals(headers, r.customHeader)
        assertEquals(data, r.customData)
    }

    @Test
    fun test_create_request_without_custom_data() {
        val METHOD = HttpMethod.POST
        val PATH = "/some/path"
        val r = VGSRequest.VGSRequestBuilder()
            .setMethod(METHOD)
            .setPath(PATH)
            .build()

        assertEquals(PATH, r.path)
        assertEquals(METHOD, r.method)
    }

    @Test
    fun test_to_network_request() {
        val BASE_URL = "base.url"
        val METHOD = HttpMethod.POST
        val PATH = "/some/path"

        val exampleRequest = HttpRequest(
            BASE_URL+PATH,
            "{}",
            emptyMap(),
            METHOD,
            HttpBodyFormat.JSON,
            60000L
        )

        val r = VGSRequest.VGSRequestBuilder()
            .setMethod(METHOD)
            .setPath(PATH)
            .build().toNetworkRequest(BASE_URL, emptyMap())

        assertEquals(exampleRequest, r)
    }

    @Test
    fun test_to_response_success() {
        val BODY = "data"
        val CODE = 200

        val exampleRequest = VGSResponse.SuccessResponse(
            body = BODY,
            code = CODE
        )

        val r = NetworkResponse(
            true,
            CODE,
            BODY
        ).toVGSResponse() as VGSResponse.SuccessResponse

        assertEquals(exampleRequest.body, r.body)
        assertEquals(exampleRequest.body, r.body)
        assertEquals(exampleRequest.code, r.code)
    }

    @Test
    fun test_to_response_failed() {
        val MESSAGE = "error text"
        val CODE = 401

        val exampleRequest = VGSResponse.ErrorResponse(
            MESSAGE,
            CODE
        )

        val r = NetworkResponse(
            code = CODE,
            message = MESSAGE
        ).toVGSResponse() as VGSResponse.ErrorResponse

        assertEquals(exampleRequest.message, r.message)
        assertEquals(exampleRequest.code, r.code)
        assertEquals(exampleRequest.body, r.body)
    }
}