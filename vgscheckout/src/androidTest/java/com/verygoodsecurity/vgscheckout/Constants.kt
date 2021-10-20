package com.verygoodsecurity.vgscheckout

object Constants {

    const val VAULT_ID = "tnt1a2b3c4y"
    const val VAULT_ID_2 = "tntshmljla7"
    const val VAULT_ID_3 = "tntpszqgikn"

    // Token
    const val INCORRECT_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50MWEyYjNjNHkiOiJURVNUIn19.5rtUsrLnS_gBHKTugiM9h9wuI9t4afupSbbdgmFHbSU"
    const val CORRECT_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNzAyMjUsImlhdCI6MTYyOTM2OTkyNSwicmVzb3VyY2VfYWNjZXNzIjp7Im11bHRpcGxleGluZy1hcHAtdG50MWEyYjNjNHkiOnsicm9sZXMiOlsiZmluYW5jaWFsLWluc3RydW1lbnRzOndyaXRlIl19fX0.n7uQ77pOMtBY99iGVg_EtXBUsgO5GZXEKSTv4kchov0"

    // Fields data
    const val VALID_CARD_HOLDER = "John Doe"
    const val VALID_CARD_NUMBER = "4111111111111111"
    const val FORMATTED_CARD_NUMBER = "4111 1111 1111 1111"
    const val INVALID_CARD_NUMBER = "0000000000000000"
    const val VALID_EXP_DATE = "10/22"
    const val INVALID_EXP_DATE = "10/2"
    const val VALID_SECURITY_CODE = "111"
    const val INVALID_SECURITY_CODE = "11"
    const val VALID_ADDRESS = "Somewhere st."
    const val VALID_CITY = "New York"
    const val USA_VALID_POSTAL_ADDRESS = "12345"
    const val INVALID_POSTAL_ADDRESS = "1234"

    const val SUCCESS_RESPONSE_CODE = 200
}