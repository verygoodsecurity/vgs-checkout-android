package com.verygoodsecurity.vgscheckout.collect.view.core.serializers

import java.util.*

internal class CountryNameToIsoSerializer : FieldDataSerializer<String, String?>() {

    private val countries = Locale.getISOCountries().associateBy { Locale("", it).displayCountry }

    override fun serialize(params: String): String {
        return countries[params] ?: params
    }
}