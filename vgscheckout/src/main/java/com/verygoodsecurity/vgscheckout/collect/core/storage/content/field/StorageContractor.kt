package com.verygoodsecurity.vgscheckout.collect.core.storage.content.field

/** @suppress */
internal interface StorageContractor<T> {
    fun checkState(state:T):Boolean
}