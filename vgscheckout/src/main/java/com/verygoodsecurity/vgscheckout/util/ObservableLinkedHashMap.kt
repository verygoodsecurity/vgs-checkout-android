package com.verygoodsecurity.vgscheckout.util

internal abstract class ObservableLinkedHashMap<K, V> constructor(
    map: LinkedHashMap<K, V> = linkedMapOf()
) : LinkedHashMap<K, V>(map) {

    protected abstract fun onChanged(map: ObservableLinkedHashMap<K, V>)

    override fun put(key: K, value: V): V? = super.put(key, value).also { onChanged(this) }
}