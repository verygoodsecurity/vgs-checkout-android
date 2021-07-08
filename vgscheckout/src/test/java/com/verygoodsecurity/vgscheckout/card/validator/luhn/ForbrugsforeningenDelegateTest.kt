package com.verygoodsecurity.vgscheckout.card.validator.luhn

import com.verygoodsecurity.vgscheckout.collect.view.card.validation.payment.brand.LuhnCheckSumValidator
import org.junit.Assert
import org.junit.Test

class ForbrugsforeningenDelegateTest {

    @Test
    fun detectForbrugsforeningen1() {
        val forb = LuhnCheckSumValidator()

        val state = forb.isValid("6007220000000004")

        Assert.assertTrue(state)
    }

}