package com.verygoodsecurity.vgscheckout.card.filter.brand

import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.VGSCardFilter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ForbTest {

    private lateinit var filter: VGSCardFilter

    @Before
    fun setupFilter() {
        filter = CardBrandFilter()
    }

    @Test
    fun test_1() {
        val brand = filter.detect("600")
        Assert.assertEquals(brand.name, CardType.FORBRUGSFORENINGEN.name)
    }
}