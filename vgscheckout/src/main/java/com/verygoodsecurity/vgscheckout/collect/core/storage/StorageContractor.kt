package com.verygoodsecurity.vgscheckout.collect.core.storage

/** @suppress */
internal interface StorageContractor<T> {
    fun checkState(state:T):Boolean
}