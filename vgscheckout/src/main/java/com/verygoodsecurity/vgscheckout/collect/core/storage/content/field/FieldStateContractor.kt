package com.verygoodsecurity.vgscheckout.collect.core.storage.content.field

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.StorageContractor
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

/** @suppress */
internal class FieldStateContractor : StorageContractor<VGSFieldState> {

    override fun checkState(state: VGSFieldState): Boolean {
        return if (state.fieldName?.trim().isNullOrEmpty()) {
            VGSCheckoutLogger.warn(InputFieldView.TAG, VGSError.FIELD_NAME_NOT_SET.message)
            false
        } else {
            true
        }
    }

}