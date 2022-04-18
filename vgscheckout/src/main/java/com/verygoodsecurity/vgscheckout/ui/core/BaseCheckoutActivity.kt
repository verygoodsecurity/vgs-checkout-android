package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.CancelEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.extension.setScreenshotsAllowed

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    NavigationHandler, ToolbarHandler {

    protected val config: C by lazy { CheckoutResultContract.Args.fromIntent<C>(intent).config }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenshotsAllowed(config.isScreenshotsAllowed)
        setContentView(R.layout.vgs_checkout_activity)
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

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        if (savedInstanceState == null) {
            initFragment()
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