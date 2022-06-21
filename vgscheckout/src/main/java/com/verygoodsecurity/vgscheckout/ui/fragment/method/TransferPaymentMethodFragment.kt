package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.content.Intent
import android.view.ViewStub
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.command.TransferCommand
import com.verygoodsecurity.vgscheckout.ui.fragment.core.ActivityResultHandler
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl
import com.verygoodsecurity.vgscheckout.util.extension.toTransferResponse
import com.verygoodsecurity.vgscheckout.util.extension.visible
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import com.verygoodsecurity.vgscheckout_google_pay.VGSCheckoutGooglePayException
import com.verygoodsecurity.vgscheckout_google_pay.VGSCheckoutGooglePayListener
import com.verygoodsecurity.vgscheckout_google_pay.VGSCheckoutGooglePayManager
import com.verygoodsecurity.vgscheckout_google_pay.VGSCheckoutGooglePayToken

internal class TransferPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutPaymentConfig>(),
    ActivityResultHandler {

    private val llWallets: LinearLayoutCompat by lazy { requireView().findViewById(R.id.llWallets) }
    private val googlePayButton: RelativeLayout by lazy { requireView().findViewById(R.id.rlGooglePay) }

    private val googlePayManager: VGSCheckoutGooglePayManager by lazy {
        VGSCheckoutGooglePayManager(
            requireContext(),
            "GATEWAY-MERCHANT-ID"
        )
    }

    override fun initView() {
        super.initView()
        initWallets()
    }

    override fun processSelectedCard(card: Card) {
        setLoading(true)

        TransferCommand(
            requireContext(),
            TransferCommand.Params(
                config.getBaseUrl(requireContext()),
                config.orderDetails?.id ?: "",
                card.finId,
                config.accessToken
            )
        ).execute(::handleTransferResult)
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            VGSCheckoutGooglePayManager.LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                googlePayManager.onLoadPaymentDataResult(
                    requestCode,
                    resultCode,
                    data,
                    object : VGSCheckoutGooglePayListener {

                        override fun onSuccess(token: VGSCheckoutGooglePayToken) {
                            VGSCheckoutLogger.debug(message = "GPay token = $token.")
                            // TODO: Use token to create fin instrument
                        }

                        override fun onError(e: VGSCheckoutGooglePayException) {
                            VGSCheckoutLogger.debug(message = "GPay error = ${e.localizedMessage}.")
                        }

                        override fun onCancel() {
                            VGSCheckoutLogger.debug(message = "GPay canceled by user.")
                        }
                    })
                googlePayButton.isClickable = true
            }
        }
    }

    private fun handleTransferResult(result: TransferCommand.Result) {
        //todo add analytic
        setLoading(false)

        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { processUserChoice() }
            return
        }

        with(resultHandler) {
            getResultBundle().putTransferResponse(result.toTransferResponse())
            getResultBundle().putIsPreSavedCard(true)
            setResult(true)
        }
    }

    private fun initWallets() {
        if (!config.isGooglePayEnabled) {
            return
        }
        googlePayManager.isReadyToPay { isReady, error ->
            if (isReady) {
                llWallets.visible()
                initGooglePayButton()
            } else {
                VGSCheckoutLogger.warn(message = "Google pay is not available, reason = $error.")
            }
        }
    }

    private fun initGooglePayButton() {
        val googlePayViewStub = requireView().findViewById<ViewStub>(R.id.vsGooglePay)
        googlePayViewStub.layoutResource = R.layout.vgs_checkout_google_pay_button
        googlePayViewStub.inflate()

        googlePayButton.setOnClickListener {
            val order = config.orderDetails
            if (order == null) {
                VGSCheckoutLogger.warn(message = "Order is null.")
                return@setOnClickListener
            }
            googlePayButton.isClickable = false
            googlePayManager.loadPaymentData(order.price, order.currency, requireActivity())
        }
    }
}