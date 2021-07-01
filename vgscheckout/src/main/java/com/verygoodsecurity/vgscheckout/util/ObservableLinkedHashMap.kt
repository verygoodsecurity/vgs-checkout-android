package com.verygoodsecurity.vgscheckout.util

internal abstract class ObservableLinkedHashMap<K, V> : LinkedHashMap<K, V>() {

    protected abstract fun onChanged(map: HashMap<K, V>)

    override fun put(key: K, value: V): V? = super.put(key, value).also { onChanged(this) }
}