package com.verygoodsecurity.vgscheckout.collect.util.extension

internal fun <T> arrayListOfNulls(maxIndex: Int): ArrayList<T?> {
    val result = ArrayList<T?>(maxIndex)
    for (i in 0..maxIndex) {
        result.add(null)
    }
    return result
}

internal inline infix fun <reified T : Any> ArrayList<T?>.overwrite(source: ArrayList<T?>): ArrayList<T?> {
    val result = arrayListOfNulls<T>(this.size.coerceAtLeast(source.size).dec())
    for (i in 0 until result.size) {
        result[i] = this.getOrNull(i)
        source.getOrNull(i)?.let { result[i] = it }
    }
    return result
}

internal fun <T> ArrayList<T>.setOrAdd(index: Int, value: T) {
    try {
        set(index, value)
    } catch (e: Exception) {
        add(value)
    }
}

@Suppress("UNCHECKED_CAST")
internal fun ArrayList<Any?>.deepMerge(
    source: ArrayList<Any?>,
    policy: ArrayMergePolicy
): ArrayList<Any?> {
    return when (policy) {
        ArrayMergePolicy.OVERWRITE -> this overwrite source
        ArrayMergePolicy.MERGE -> {
            source.forEachIndexed { index, value ->
                when {
                    value is Map<*, *> && this.getOrNull(index) is Map<*, *> -> {
                        val targetValue = (this[index] as Map<String, Any>).toMutableMap()
                        if (targetValue.keys.containsAll(value.keys)) {
                            this[index] = targetValue.deepMerge(value as Map<String, Any>, policy)
                        } else {
                            add(index, value)
                        }
                    }
                    value is Map<*, *> -> this.add(index, value)//fixme this.setOrAdd(value, index)
                    value != null -> add(index, value)
                }
            }
            this
        }
    }
}