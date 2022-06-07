package com.verygoodsecurity.vgscheckout.demo.orchestrtion.payment

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.demo.orchestrtion.OrchestrationCheckoutActivity

class PaymentCheckoutActivity : OrchestrationCheckoutActivity() {

    override fun initializeConfiguration(
        token: String,
        callback: (config: CheckoutConfig) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}