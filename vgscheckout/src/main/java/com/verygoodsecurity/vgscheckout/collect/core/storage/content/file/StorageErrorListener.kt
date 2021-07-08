package com.verygoodsecurity.vgscheckout.collect.core.storage.content.file

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError

internal interface StorageErrorListener {
    fun onStorageError(error: VGSError)
}