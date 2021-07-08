package com.verygoodsecurity.vgscheckout.card.filter.brand

import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.VGSCardFilter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class JcbTest {

    private lateinit var filter: VGSCardFilter

    @Before
    fun setupFilter() {
        filter = CardBrandFilter()
    }

    @Test
    fun test_1() {
        val brand = filter.detect("35")
        Assert.assertEquals(brand.name, CardType.JCB.name)
    }

    @Test
    fun test_2() {
        val brand = filter.detect("350")
        Assert.assertEquals(brand.name, CardType.JCB.name)
    }

    @Test
    fun test_3() {
        val brand = filter.detect("359")
        Assert.assertEquals(brand.name, CardType.JCB.name)
    }

}