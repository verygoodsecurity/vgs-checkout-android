package com.verygoodsecurity.vgscheckout.card.filter

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.card.BrandParams
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.MutableCardFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CustomCardBrandsFilteringTest {

    private lateinit var filter: MutableCardFilter

    @Before
    fun setupFilter() {
        filter = CardBrandFilter()
    }

    @Test
    fun test_detect_custom_brand() {
        val c1 = CardBrand("^123", "VG_Search", drawableResId = R.drawable.vgs_checkout_ic_card_front_preview)
        val c2 = CardBrand("^777", "VGS", drawableResId = R.drawable.vgs_checkout_ic_jcb)

        filter.addCustomCardBrand(c1)
        filter.addCustomCardBrand(c2)

        val testItem1 = filter.detect("1234561112335412")
        assertEquals(testItem1?.resId, R.drawable.vgs_checkout_ic_card_front_preview)
    }

    @Test
    fun test_detect_custom_brand_2() {
        val c1 = CardBrand("^123", "VG_Search", drawableResId = R.drawable.vgs_checkout_ic_card_front_preview)
        val c2 = CardBrand("^777", "VGS", drawableResId = R.drawable.vgs_checkout_ic_jcb)

        filter.addCustomCardBrand(c1)
        filter.addCustomCardBrand(c2)

        val testItem1 = filter.detect("1234561112335412")
        assertEquals(testItem1?.resId, R.drawable.vgs_checkout_ic_card_front_preview)
    }

    @Test
    fun test_detect_custom_brand_mask() {
        val regex = "^12333"
        val name = "VG_Search"
        val resId = R.drawable.vgs_checkout_ic_card_front_preview

        val mask = "### ### ### #####"
        val params = BrandParams(mask)

        val c1 = CardBrand(regex, name, resId, params)

        filter.addCustomCardBrand(c1)

        val testItem1 = filter.detect("12333611123354")

        assertNotNull(testItem1)
        assertEquals(CardType.UNKNOWN, testItem1!!.cardType)
        assertEquals(regex, testItem1.regex)
        assertEquals(name, testItem1.name)
        assertEquals(mask, testItem1.currentMask)
        assertEquals(resId, R.drawable.vgs_checkout_ic_card_front_preview)
    }

    @Test
    fun test_detect_custom_brand_divider() {
        (filter as CardBrandFilter).setDivider("-")

        val c1 = CardBrand("^123", "VG_Search", drawableResId = R.drawable.vgs_checkout_ic_card_front_preview)
        val c2 = CardBrand("^777", "VGS", drawableResId = R.drawable.vgs_checkout_ic_jcb)

        filter.addCustomCardBrand(c1)
        filter.addCustomCardBrand(c2)

        val testItem1 = filter.detect("1234-5611-1233-5412")
        assertEquals(testItem1?.resId, R.drawable.vgs_checkout_ic_card_front_preview)
    }

    @Test
    fun test_detect_custom_brand_divider_2() {
        (filter as CardBrandFilter).setDivider("-")

        val regex = "^12333"
        val name = "VG_Search"
        val resId = R.drawable.vgs_checkout_ic_card_front_preview

        val mask = "### ### ### #####"
        val params = BrandParams(mask)

        val c1 = CardBrand(regex, name, resId, params)

        filter.addCustomCardBrand(c1)

        val testItem1 = filter.detect("123-336-111-23354")

        assertNotNull(testItem1)
        assertEquals(CardType.UNKNOWN, testItem1!!.cardType)
        assertEquals(regex, testItem1.regex)
        assertEquals(name, testItem1.name)
        assertEquals(mask, testItem1.currentMask)
        assertEquals(resId, R.drawable.vgs_checkout_ic_card_front_preview)
    }
}