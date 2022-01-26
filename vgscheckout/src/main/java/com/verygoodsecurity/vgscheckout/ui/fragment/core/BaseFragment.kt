package com.verygoodsecurity.vgscheckout.ui.fragment.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.ui.core.ErrorHandler
import com.verygoodsecurity.vgscheckout.ui.core.NavigationHandler
import com.verygoodsecurity.vgscheckout.ui.core.ToolbarHandler
import com.verygoodsecurity.vgscheckout.util.CurrencyFormatter
import com.verygoodsecurity.vgscheckout.util.extension.requireParcelable

internal abstract class BaseFragment<C : CheckoutConfig> : Fragment, LoadingHandler {

    constructor() : super()

    constructor(@LayoutRes id: Int) : super(id)

    protected val config: C by lazy { requireParcelable(KEY_BUNDLE_CONFIG) }
    protected val title: String by lazy { generateButtonTitle() }

    protected lateinit var navigationHandler: NavigationHandler
    protected lateinit var toolbarHandler: ToolbarHandler
    protected lateinit var errorHandler: ErrorHandler

    abstract fun getToolbarTitle(): String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationHandler = requireActivity() as NavigationHandler
        toolbarHandler = requireActivity() as ToolbarHandler
        errorHandler = requireActivity() as ErrorHandler
    }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
    }

    private fun generateButtonTitle(): String = when (config) {
        is VGSCheckoutPaymentConfig -> {
            with(config as VGSCheckoutPaymentConfig) {
                val amount = CurrencyFormatter.format(paymentInfo.amount, paymentInfo.currency)
                getString(R.string.vgs_checkout_button_pay_title, amount)
            }
        }
        else -> getString(R.string.vgs_checkout_button_save_card_title)
    }

    private fun updateToolbarTitle() {
        toolbarHandler.setTitle(getToolbarTitle())
    }

    companion object {

        private const val KEY_BUNDLE_CONFIG = "com.verygoodsecurity.vgscheckout.config"

        inline fun <reified T : BaseFragment<*>> create(config: CheckoutConfig): BaseFragment<*> =
            T::class.java.newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_BUNDLE_CONFIG, config)
                }
            }
    }
}