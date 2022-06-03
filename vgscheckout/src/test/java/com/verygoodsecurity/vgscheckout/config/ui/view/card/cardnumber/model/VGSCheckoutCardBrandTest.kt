package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model

import com.verygoodsecurity.vgscheckout.test.R
import org.junit.Assert
import org.junit.Test


internal class VGSCheckoutCardBrandTest {

    @Test
    fun getBrandIcon_elo_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_elo
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("elo"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Elo"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("ELO"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" E LO "))
    }

    @Test
    fun getBrandIcon_visa_electron_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_visa_electron
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("visa_electron"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa_Electron"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa_Electron"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("VISA_ELECTRON"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" VISA ELECTRON "))
    }

    @Test
    fun getBrandIcon_maestro_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_maestro
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("maestro"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Maestro"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("MAESTRO"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" MAES TRO "))
    }

    @Test
    fun getBrandIcon_forbrugsforeningen_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_forbrugsforeningen
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("forbrugsforeningen"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Forbrugsforeningen"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("FORBRUGSFORENINGEN"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" FOR BRUG SFORENIN GEN "))
    }

    @Test
    fun getBrandIcon_dankort_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_dankort
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("dankort"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Dankort"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DANKORT"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DAN KORT "))
    }

    @Test
    fun getBrandIcon_visa_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_visa
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("visa"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("VISA"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" VI SA "))
    }

    @Test
    fun getBrandIcon_mastercard_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_mastercard
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("mastercard"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Mastercard"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("MASTERCARD"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" MASTER CARD "))
    }

    @Test
    fun getBrandIcon_amex_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_amex
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("AmEx"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("americanexpress"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("american_express"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("AMERICAN_EXPRESS"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" AMERICAN EXPRESS "))
    }

    @Test
    fun getBrandIcon_hipercard_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_hipercard
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("hipercard"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Hipercard"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("HIPERCARD"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" HI PER CARD "))
    }

    @Test
    fun getBrandIcon_dinclub_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_diners
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("dinclub"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Dinclub"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DINCLUB"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DIN CLUB "))
    }

    @Test
    fun getBrandIcon_discover_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_discover
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("discover"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Discover"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DISCOVER"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DIS COVER "))
    }

    @Test
    fun getBrandIcon_unionpay_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_union_pay
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("unionpay"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Unionpay"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("UNIONPAY"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" UNIO NPAY "))
    }

    @Test
    fun getBrandIcon_jcb_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_jcb
        // Assert
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("jcb"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Jcb"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("JCB"))
        Assert.assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" JC B "))
    }
}