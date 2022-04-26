package com.verygoodsecurity.vgscheckout.config.payment

/**
 * Additional payment options.
 *
 * @param paymentMethod specifies available payment method [VGSCheckoutPaymentMethod].
 */
class VGSCheckoutPaymentOptions constructor(
    val paymentMethod: VGSCheckoutPaymentMethod = VGSCheckoutPaymentMethod.NewCard
)