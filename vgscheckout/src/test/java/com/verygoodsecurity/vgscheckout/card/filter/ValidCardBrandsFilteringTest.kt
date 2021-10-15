package com.verygoodsecurity.vgscheckout.card.filter

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.util.extension.toCardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.CardBrandFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.MutableCardFilter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ValidCardBrandsFilteringTest {

    private lateinit var filter: MutableCardFilter

    @Before
    fun setupFilter() {
        filter = CardBrandFilter()
    }

    @Test
    fun setValidCardBrands_visa_otherBrandsNotDetected() {
        // Arrange
        filter.setValidCardBrands(listOf(CardType.VISA.toCardBrand()))
        // Act
        val visaResult = filter.detect("4111-1111-1111-1111")
        val dankort = filter.detect("5019-0000-0000-0000")
        // Assert
        Assert.assertEquals(visaResult.name, "VISA")
        Assert.assertEquals(dankort.name, "UNKNOWN")
    }

    @Test
    fun setValidCardBrands_customBrandAndVISA_otherBrandsNotDetected() {
        // Arrange
        val customBrand = CardBrand("^777", "VGS", drawableResId = R.drawable.vgs_checkout_ic_jcb_light)
        filter.setValidCardBrands(listOf(customBrand, CardType.VISA.toCardBrand()))
        // Act
        val visaResult = filter.detect("4111-1111-1111-1111")
        val customBrandResultResult = filter.detect("7771-1111-1111-1111")
        val dankort = filter.detect("5019-0000-0000-0000")
        // Assert
        Assert.assertEquals(visaResult.name, "VISA")
        Assert.assertEquals(customBrandResultResult.name, "VGS")
        Assert.assertEquals(dankort.name, "UNKNOWN")
    }
}