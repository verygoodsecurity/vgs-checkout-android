package com.verygoodsecurity.vgscheckout.storage.fields

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.field.FieldStateContractor
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FieldsContractorTest {

    private var contractor: FieldStateContractor = FieldStateContractor()

    @Test
    fun test_empty_field_name_true() {
        val state = VGSFieldState(fieldName = "name")
        assertTrue(contractor.checkState(state))
    }

    @Test
    fun test_empty_field_name_false() {
        val state1 = VGSFieldState(fieldName = " ")
        assertFalse(contractor.checkState(state1))

        val state2 = VGSFieldState()
        assertFalse(contractor.checkState(state2))
    }
}