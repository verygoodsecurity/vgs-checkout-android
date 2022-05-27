package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.verygoodsecurity.vgscheckout.test.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VGSCheckoutCardBrandTest {

    @Test
    fun getBrandIcon_elo_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_elo
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("elo"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Elo"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("ELO"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" E LO "))
    }

    @Test
    fun getBrandIcon_visa_electron_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_visa_electron
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("visa_electron"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa_Electron"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa_Electron"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("VISA_ELECTRON"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" VISA ELECTRON "))
    }

    @Test
    fun getBrandIcon_maestro_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_maestro
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("maestro"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Maestro"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("MAESTRO"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" MAES TRO "))
    }

    @Test
    fun getBrandIcon_forbrugsforeningen_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_forbrugsforeningen_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("forbrugsforeningen"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Forbrugsforeningen"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("FORBRUGSFORENINGEN"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" FOR BRUG SFORENIN GEN "))
    }

    @Test
    fun getBrandIcon_dankort_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_dankort_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("dankort"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Dankort"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DANKORT"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DAN KORT "))
    }

    @Test
    fun getBrandIcon_visa_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_visa
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("visa"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Visa"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("VISA"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" VI SA "))
    }

    @Test
    fun getBrandIcon_mastercard_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_mastercard_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("mastercard"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Mastercard"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("MASTERCARD"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" MASTER CARD "))
    }

    @Test
    fun getBrandIcon_amex_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_amex
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("AmEx"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("americanexpress"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("american_express"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("AMERICAN_EXPRESS"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" AMERICAN EXPRESS "))
    }

    @Test
    fun getBrandIcon_hipercard_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_hipercard_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("hipercard"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Hipercard"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("HIPERCARD"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" HI PER CARD "))
    }

    @Test
    fun getBrandIcon_dinclub_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_diners_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("dinclub"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Dinclub"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DINCLUB"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DIN CLUB "))
    }

    @Test
    fun getBrandIcon_discover_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_discover_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("discover"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Discover"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("DISCOVER"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" DIS COVER "))
    }

    @Test
    fun getBrandIcon_unionpay_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_union_pay_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("unionpay"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Unionpay"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("UNIONPAY"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" UNIO NPAY "))
    }

    @Test
    fun getBrandIcon_jcb_successful() {
        // Arrange
        val expected = R.drawable.vgs_checkout_ic_jcb_dark
        // Assert
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("jcb"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("Jcb"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon("JCB"))
        assertEquals(expected, VGSCheckoutCardBrand.getBrandIcon(" JC B "))
    }
}