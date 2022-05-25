package com.verygoodsecurity.vgscheckout.card.connection

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputCardNumberConnection
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputRunnable
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandPreview
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandFilter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock


class InputCardNumberConnectionTest {
    private lateinit var connection: InputRunnable
    private lateinit var iCardBrand: InputCardNumberConnection.IDrawCardBrand

    @Before
    fun setupConnection() {
        val divider = " "
        val client = getValidator()

        iCardBrand = getCardBrandPreviewListener()
        connection =
            InputCardNumberConnection(
                0,
                client,
                iCardBrand,
                divider
            )

        setupFilter(connection, divider)
    }

    @Test
    fun test_connection() {
        val client = getValidator()
        val connection: InputRunnable =
            InputCardNumberConnection(
                0,
                client
            )

        setupFilter(connection, null)

        val state = createFieldStateMastercard("")
        connection.setOutput(state)

        connection.run()

        val filteredContent = connection.getOutput().content as FieldContent.CardNumberContent
        assertEquals(filteredContent.cardtype, CardType.MASTERCARD)
        assertTrue(connection.getOutput().isValid)
    }

    @Test
    fun test_connection_divider() {
        val divider = "-"

        val client = getValidator()
        val connection: InputRunnable =
            InputCardNumberConnection(
                0,
                client,
                divider = divider
            )

        setupFilter(connection, divider)

        val state = createFieldStateMastercard(divider)
        connection.setOutput(state)

        connection.run()

        val filteredContent = connection.getOutput().content as FieldContent.CardNumberContent
        assertEquals(filteredContent.cardtype, CardType.MASTERCARD)
        assertTrue(connection.getOutput().isValid)
    }

    @Test
    fun test_draw_card_icon() {
        connection.run()

        Mockito.verify(iCardBrand).onCardBrandPreview(CardBrandPreview(CardType.UNKNOWN, CardType.UNKNOWN.regex, CardType.UNKNOWN.name, CardType.UNKNOWN.resId))

        val state = createFieldStateVisa(" ")
        connection.setOutput(state)

        connection.run()

        val c = CardBrandPreview(CardType.VISA,
            CardType.VISA.regex,
            CardType.VISA.name,
            CardType.VISA.resId,
            CardType.VISA.mask,
            CardType.VISA.algorithm,
            CardType.VISA.rangeNumber,
            CardType.VISA.rangeCVV)
        Mockito.verify(iCardBrand).onCardBrandPreview(c)
    }

    private fun getCardBrandPreviewListener(): InputCardNumberConnection.IDrawCardBrand {
        return mock(InputCardNumberConnection.IDrawCardBrand::class.java)
    }

    private fun getValidator():VGSValidator {
        val client = mock(VGSValidator::class.java)
        Mockito.doReturn(true).`when`(client).isValid(Mockito.anyString())

        return client
    }

    private fun setupFilter(
        connection: InputRunnable,
        divider: String?
    ) {
        val filter = CardBrandFilter(divider)
        connection.addFilter(filter)
    }

    private fun createFieldStateMastercard(divider:String):VGSFieldState {
        val content = FieldContent.CardNumberContent()
        content.data = "5555${divider}5555${divider}5555${divider}4444"
        val textItem = VGSFieldState(isValid = false,
            isRequired = true,
            fieldName = "fieldName",
            content = content)

        return textItem
    }

    private fun createFieldStateVisa(divider:String):VGSFieldState {
        val content = FieldContent.CardNumberContent()
        content.data = "4111${divider}1111${divider}1111${divider}1111"
        val textItem = VGSFieldState(isValid = false,
            isRequired = false,
            fieldName = "fieldName",
            content = content)

        return textItem
    }

    private fun <T> any(): T = Mockito.any<T>()
}