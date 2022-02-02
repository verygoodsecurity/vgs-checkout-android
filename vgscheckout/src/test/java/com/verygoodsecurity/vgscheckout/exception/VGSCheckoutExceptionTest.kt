package com.verygoodsecurity.vgscheckout.exception

import com.verygoodsecurity.vgscheckout.exception.internal.*
import org.junit.Assert
import org.junit.Test

class VGSCheckoutExceptionTest {

    @Test
    fun check_VGSCheckoutPaymentInfoParseException_code() {
        Assert.assertEquals(2003, PaymentInfoParseException(Exception()).code)
    }

    @Test
    fun check_VGSCheckoutJWTParseException_code() {
        Assert.assertEquals(2000, JWTParseException().code)
    }

    @Test
    fun check_VGSCheckoutJWTRestrictedRoleException_code() {
        Assert.assertEquals(2001, JWTRestrictedRoleException("").code)
    }

    @Test
    fun check_VGSCheckoutFinIdNotFoundException_code() {
        Assert.assertEquals(2002, FinIdNotFoundException(Exception()).code)
    }

    @Test
    fun check_VGSCheckoutResultParseException_code() {
        Assert.assertEquals(1002, ResultParseException().code)
    }
}