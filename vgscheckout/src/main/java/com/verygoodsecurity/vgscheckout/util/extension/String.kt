package com.verygoodsecurity.vgscheckout.util.extension

import okio.ByteString.Companion.decodeBase64
import org.json.JSONException
import org.json.JSONObject

private const val JWT_TOKEN_DELIMITER = "."
private const val PAYLOAD_INDEX = 1

internal fun String.decodeJwtPayload(): String? =
    this.split(JWT_TOKEN_DELIMITER).getOrNull(PAYLOAD_INDEX)?.decodeBase64()?.utf8()

internal fun String.toJson(): JSONObject? = try {
    JSONObject(this)
} catch (e: JSONException) {
    null
}
