package com.verygoodsecurity.vgscheckout.collect.core.storage.content.field

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.StorageContractor
import com.verygoodsecurity.vgscheckout.util.logger.VGSCollectLogger
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

/** @suppress */
internal class FieldStateContractor(
    private val context: Context
) : StorageContractor<VGSFieldState> {

    override fun checkState(state: VGSFieldState): Boolean {
        return if (state.fieldName?.trim().isNullOrEmpty()) {
            val message = context.getString(VGSError.FIELD_NAME_NOT_SET.messageResId)
            VGSCollectLogger.warn(InputFieldView.TAG, message)
            false
        } else {
            true
        }
    }

}