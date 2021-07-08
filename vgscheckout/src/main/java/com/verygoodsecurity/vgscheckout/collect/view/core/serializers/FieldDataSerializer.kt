package com.verygoodsecurity.vgscheckout.collect.view.core.serializers

internal abstract class FieldDataSerializer<P, R> {

    internal abstract fun serialize(params: P): R
}