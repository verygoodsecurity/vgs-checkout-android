package com.verygoodsecurity.vgscheckout.api

import com.verygoodsecurity.vgscheckout.collect.core.api.DefaultApiClientStorage
import org.junit.Assert
import org.junit.Test

class VgsApiTemporaryStorageTest {

    @Test
    fun testTempStoreCustomData() {
        val storage = DefaultApiClientStorage()

        Assert.assertEquals(0, storage.getCustomData().size)

        val data = HashMap<String, String>()
        data["key"] = "value"
        storage.setCustomData(data)

        Assert.assertEquals(1, storage.getCustomData().size)
    }

    @Test
    fun testTempStoreCustomHeaders() {
        val storage = DefaultApiClientStorage()

        Assert.assertEquals(0, storage.getCustomHeaders().size)

        val headers = HashMap<String, String>()
        headers["key"] = "value"
        storage.setCustomHeaders(headers)

        Assert.assertEquals(1, storage.getCustomHeaders().size)
    }

    @Test
    fun testTempStoreCustomDataReset() {
        val storage = DefaultApiClientStorage()

        val data = HashMap<String, String>()
        data["key"] = "value"
        storage.setCustomData(data)

        storage.resetCustomData()

        Assert.assertEquals(0, storage.getCustomData().size)
    }

    @Test
    fun testTempStoreCustomHeadersReset() {
        val storage = DefaultApiClientStorage()

        val data = HashMap<String, String>()
        data["key"] = "value"
        storage.setCustomHeaders(data)

        storage.resetCustomHeaders()

        Assert.assertEquals(0, storage.getCustomHeaders().size)
    }

    @Test
    fun testTempStoreGetCustomData() {
        val key = "anyKey"
        val value = "anyValue"

        val storage = DefaultApiClientStorage()

        Assert.assertEquals(0, storage.getCustomData().size)

        val data = HashMap<String, String>()
        data[key] = value
        storage.setCustomData(data)

        val testValue = storage.getCustomData()[key]

        Assert.assertEquals(value, testValue)
    }

    @Test
    fun testTempStoreGetCustomHeaders() {
        val key = "anyKey"
        val value = "anyValue"

        val storage = DefaultApiClientStorage()

        Assert.assertEquals(0, storage.getCustomHeaders().size)

        val data = HashMap<String, String>()
        data[key] = value
        storage.setCustomHeaders(data)

        val testValue = storage.getCustomHeaders()[key]

        Assert.assertEquals(value, testValue)
    }
}