package com.verygoodsecurity.vgscheckout.card.connection

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputCardExpDateConnection
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputRunnable
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class InputCardExpDateConnectionTest {
    private val connection: InputRunnable by lazy {
        val client = Mockito.mock(VGSValidator::class.java)
        Mockito.doReturn(true).`when`(client).isValid(Mockito.anyString())
        InputCardExpDateConnection(
            0,
            client
        )
    }

    @Test
    fun emitEmptyNotRequired() {
        val content = FieldContent.InfoContent()
        content.data = ""
        val textItem = VGSFieldState(isValid = false,
            isRequired = false,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()

        assertTrue(connection.getOutput().isValid)
    }

    @Test
    fun emitNotRequired() {
        val content = FieldContent.InfoContent()
        content.data = "testStr"
        val textItem = VGSFieldState(isValid = false,
            isRequired = true,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()

        assertTrue(connection.getOutput().isValid)
    }

    private fun <T> any(): T = Mockito.any<T>()
}