package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.app.Activity
import android.content.Intent
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.command.TransferCommand
import com.verygoodsecurity.vgscheckout.ui.fragment.core.ActivityResultHandler
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl
import com.verygoodsecurity.vgscheckout.util.extension.toTransferResponse
import com.verygoodsecurity.vgscheckout.util.extension.visible
import com.verygoodsecurity.vgscheckout.util.googlepay.Manager
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal class TransferPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutPaymentConfig>(),
    ActivityResultHandler {

    private val llWallets: LinearLayoutCompat by lazy { requireView().findViewById(R.id.llWallets) }
    private val googlePayButton: RelativeLayout by lazy { requireView().findViewById(R.id.rlGooglePay) }

    private val googlePayManager: Manager by lazy { Manager(requireContext()) }

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
            Manager.LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let {
                                VGSCheckoutLogger.debug(message = "Google pay flow success, data = ${it.toJson()}")
                            }
                        }
                    Activity.RESULT_CANCELED -> VGSCheckoutLogger.debug(message = "Google pay flow canceled.")
                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            VGSCheckoutLogger.warn(message = "Google pay flow failed, status = $it")
                        }
                    }
                }
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
        googlePayButton.setOnClickListener {
            val order = config.orderDetails
            if (order == null) {
                VGSCheckoutLogger.warn(message = "Order is null.")
                return@setOnClickListener
            }
            googlePayButton.isClickable = false
            googlePayManager.loadPaymentData(order, requireActivity())
        }
    }
}