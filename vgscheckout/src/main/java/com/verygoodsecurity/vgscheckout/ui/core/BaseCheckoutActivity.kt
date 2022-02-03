package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.CancelEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.extension.findFragmentByTag
import com.verygoodsecurity.vgscheckout.util.extension.setScreenshotsAllowed

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    FragmentOnAttachListener, NavigationHandler, ToolbarHandler, OnAddCardResponseListener {

    protected val config: C by lazy { CheckoutResultContract.Args.fromIntent<C>(intent).config }

    protected lateinit var loadingHandler: LoadingHandler

    protected val resultBundle = VGSCheckoutResultBundle()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var shouldHandleAddCard: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenshotsAllowed(config.isScreenshotsAllowed)
        setContentView(R.layout.vgs_checkout_activity)
        supportFragmentManager.addFragmentOnAttachListener(this)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        config.analyticTracker.log(CancelEvent)
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onAttachFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        loadingHandler = fragment as LoadingHandler
    }

    override fun navigateToSaveCard() {
        val fragment = BaseFragment.create<SaveCardFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

    override fun navigateToPaymentMethods() {
        val fragment = BaseFragment.create<SelectPaymentMethodFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onAddCardResponse(response: VGSCheckoutAddCardResponse, shouldSaveCard: Boolean?) {
        if (!shouldHandleAddCard) {
            return
        }
        resultBundle.putAddCardResponse(response)
        shouldSaveCard?.let { resultBundle.putShouldSaveCard(shouldSaveCard) }
        if (response.isSuccessful) {
            handleSuccessfulAddCardResponse(response)
        } else {
            sendResult(VGSCheckoutResult.Failed(resultBundle))
        }
    }

    protected open fun handleSuccessfulAddCardResponse(response: VGSCheckoutAddCardResponse) {
        sendResult(VGSCheckoutResult.Success(resultBundle))
    }

    protected fun sendResult(response: VGSCheckoutResult) {
        val resultBundle = CheckoutResultContract.Result(response).toBundle()
        setResult(Activity.RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        if (savedInstanceState == null) {
            initFragment()
        } else {
            loadingHandler = findFragmentByTag(FRAGMENT_TAG) as LoadingHandler
        }
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    protected open fun initFragment() {
        navigateToSaveCard()
    }

    companion object {

        const val FRAGMENT_TAG = "com.verygoodsecurity.vgscheckout.fragment_tag"
    }
}