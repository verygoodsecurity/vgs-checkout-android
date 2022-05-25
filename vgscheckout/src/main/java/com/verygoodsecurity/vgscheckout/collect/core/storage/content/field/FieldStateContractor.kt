package com.verygoodsecurity.vgscheckout.collect.core.storage.content.field

import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.exception.internal.FieldNameNotSetException
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

/** @suppress */
internal class FieldStateContractor : StorageContractor<VGSFieldState> {

    //todo move warning to custom config.
    override fun checkState(state: VGSFieldState): Boolean {
        return if (state.fieldName?.trim().isNullOrEmpty()) {
            VGSCheckoutLogger.warn(InputFieldView.TAG, FieldNameNotSetException())
            false
        } else {
            true
        }
    }

}