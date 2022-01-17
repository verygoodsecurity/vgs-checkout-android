package com.verygoodsecurity.payments.data.source.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class GetAccessTokenResponse constructor(
    @SerializedName("access_token") val token: String
)