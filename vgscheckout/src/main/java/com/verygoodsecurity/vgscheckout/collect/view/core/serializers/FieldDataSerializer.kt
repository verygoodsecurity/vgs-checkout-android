package com.verygoodsecurity.vgscheckout.collect.view.core.serializers

abstract class FieldDataSerializer<P, R> {

    internal abstract fun serialize(params: P): R
}