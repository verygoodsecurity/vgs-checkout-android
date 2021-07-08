package com.verygoodsecurity.vgscheckout.collect.core.storage

/** @suppress */
interface StorageContractor<T> {
    fun checkState(state:T):Boolean
}