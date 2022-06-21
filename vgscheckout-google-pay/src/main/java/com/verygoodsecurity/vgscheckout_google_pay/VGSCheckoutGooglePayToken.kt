package com.verygoodsecurity.vgscheckout_google_pay

data class VGSCheckoutGooglePayToken constructor(
    val signature: String,
    val intermediateSigningKey: IntermediateSigningKey,
    val protocolVersion: String,
    val signedMessage: SignedMessage,
) {

    data class IntermediateSigningKey constructor(
        val signedKey: SignedKey,
        val signatures: List<String>
    ) {

        data class SignedKey constructor(
            val keyValue: String,
            val keyExpiration: String
        )
    }

    data class SignedMessage constructor(
        val encryptedMessage: String,
        val ephemeralPublicKey: String,
        val tag: String
    )
}