package com.verygoodsecurity.vgscheckout.model.response

import com.verygoodsecurity.vgscheckout.model.response.core.VGSCheckoutResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class VGSCheckoutCardResponse(
    override val isSuccessful: Boolean,
    override val code: Int,
    override val body: String?,
    override val message: String?
) : VGSCheckoutResponse()