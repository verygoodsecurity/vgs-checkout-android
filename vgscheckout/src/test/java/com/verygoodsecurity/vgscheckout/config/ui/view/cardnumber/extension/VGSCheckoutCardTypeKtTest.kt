package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.extension

import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutBrandParams
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardType
import org.junit.Assert.assertEquals
import org.junit.Test

class VGSCheckoutCardTypeKtTest {

    private val jbcCardBrand = VGSCheckoutCardBrand(
        "^35",
        VGSCheckoutCardType.JCB.name,
        VGSCheckoutCardType.JCB.resId,
        VGSCheckoutBrandParams(
            VGSCheckoutCardType.JCB.mask,
            VGSCheckoutCardType.JCB.algorithm,
            VGSCheckoutCardType.JCB.rangeNumber,
            VGSCheckoutCardType.JCB.rangeCVV,
        ),
        VGSCheckoutCardType.JCB
    )

    private val visaCardBrand = VGSCheckoutCardBrand(
        VGSCheckoutCardType.VISA.regex,
        VGSCheckoutCardType.VISA.name,
        VGSCheckoutCardType.VISA.resId,
        VGSCheckoutBrandParams(
            VGSCheckoutCardType.VISA.mask,
            VGSCheckoutCardType.VISA.algorithm,
            VGSCheckoutCardType.VISA.rangeNumber,
            VGSCheckoutCardType.VISA.rangeCVV,
        ),
        VGSCheckoutCardType.VISA
    )

    @Test
    fun toCardBrand_successfullyMapped() {
        // Act
        val jbcCardTypeToCardBrand = VGSCheckoutCardType.JCB.toCardBrand()
        // Assert
        assertEquals(jbcCardTypeToCardBrand, jbcCardBrand)
    }

    @Test
    fun toCardBrands_successfullyMapped() {
        // Arrange
        val expectedResult = listOf(jbcCardBrand, visaCardBrand)
        // Act
        val result = arrayOf(VGSCheckoutCardType.JCB, VGSCheckoutCardType.VISA).toCardBrands()
        // Assert
        assertEquals(result, expectedResult)
    }
}