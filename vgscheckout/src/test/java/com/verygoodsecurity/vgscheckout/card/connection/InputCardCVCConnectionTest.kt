package com.verygoodsecurity.vgscheckout.card.connection

import com.verygoodsecurity.vgscheckout.collect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputCardCVCConnection
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputRunnable
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class InputCardCVCConnectionTest {
    val connection: InputRunnable by lazy {
        val client = mock(VGSValidator::class.java)
        doReturn(true).`when`(client).isValid(anyString())
        InputCardCVCConnection(
            0,
            client
        )
    }

    @Test
    fun setChangeListener() {
        val listener = mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)
        verify(listener, times(1)).emit(anyInt(), any())
    }

    @Test
    fun emitItem() {
        val listener = mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)
        verify(listener, times(1)).emit(anyInt(), any())
    }

    @Test
    fun setOutput() {
        val listener = mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val textItem = VGSFieldState(fieldName = "fieldName")
        connection.setOutput(textItem)

        connection.run()
        verify(listener).emit(0, textItem)
    }

    @Test
    fun emitEmptyNotRequired() {
        val listener = mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val content = FieldContent.InfoContent()
        content.data = ""
        val textItem = VGSFieldState(isValid = false,
            isRequired = false,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()
        verify(listener).emit(0, textItem)
    }

    @Test
    fun emitNotRequired() {
        val listener = mock(OnVgsViewStateChangeListener::class.java)
        connection.setOutputListener(listener)

        val content = FieldContent.InfoContent()
        content.data = "testStr"
        val textItem = VGSFieldState(isValid = false,
            isRequired = true,
            fieldName = "fieldName",
            content = content)
        connection.setOutput(textItem)

        connection.run()
        verify(listener).emit(0, textItem)
    }

    private fun <T> any(): T = Mockito.any<T>()
}