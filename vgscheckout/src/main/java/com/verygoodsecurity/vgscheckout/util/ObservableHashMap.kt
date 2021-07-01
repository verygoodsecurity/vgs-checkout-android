package com.verygoodsecurity.vgscheckout.util

internal abstract class ObservableHashMap<K, V> : HashMap<K, V>() {

    protected abstract fun onChanged(map: HashMap<K, V>)

    override fun put(key: K, value: V): V? = super.put(key, value).also { onChanged(this) }
}