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
import com.google.android.material.snackbar.Snackbar
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.CancelEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.save.core.BaseSaveCardFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.core.BaseSaveCardFragment.Companion.TAG
import com.verygoodsecurity.vgscheckout.ui.fragment.save.core.InputViewBinder
import com.verygoodsecurity.vgscheckout.ui.fragment.save.core.ValidationResultListener
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.*

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    FragmentOnAttachListener, InputViewBinder, ValidationResultListener,
    VgsCollectResponseListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect: VGSCollect by lazy {
        CollectProvider().get(this, config).apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    protected lateinit var loadingHandler: LoadingHandler

    abstract fun resolveConfig(intent: Intent): C

    abstract fun hasCustomHeaders(): Boolean

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

    override fun bind(vararg view: InputFieldView) {
        collect.bindView(*view)
    }

    override fun unbind(vararg view: InputFieldView) {
        collect.unbindView(*view)
    }

    override fun onFailed(invalidFieldsAnalyticsNames: List<String>) {
        sendRequestEvent(invalidFieldsAnalyticsNames)
    }

    override fun onSuccess() {
        loadingHandler.setIsLoading(true)
        makeRequest()
    }

    override fun onResponse(response: VGSResponse) {
        if (isNetworkConnectionError(response)) {
            loadingHandler.setIsLoading(false)
            showNetworkConnectionErrorSnackBar()
            return
        }
        handleResponse(response)
    }

    protected open fun handleResponse(response: VGSResponse) {
        sendResult(response.toCheckoutResult())
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
            showSaveCardFragment()
        } else {
            loadingHandler = supportFragmentManager.findFragmentByTag(TAG) as LoadingHandler
        }
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    private fun showSaveCardFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fcvContainer, BaseSaveCardFragment.create(config.formConfig), TAG)
            .commit()
    }

    private fun makeRequest() {
        with(config.routeConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath(path)
                    .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                    .setCustomData(requestOptions.extraData)
                    .setCustomHeader(requestOptions.extraHeaders)
                    .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                    .build()
            )
        }
        sendRequestEvent()
    }

    private fun sendRequestEvent(invalidFields: List<String> = emptyList()) {
        with(config) {
            analyticTracker.log(
                RequestEvent(
                    invalidFields.isEmpty(),
                    routeConfig.hostnamePolicy is VGSCheckoutHostnamePolicy.CustomHostname,
                    routeConfig.requestOptions.extraData.isNotEmpty(),
                    hasCustomHeaders(),
                    formConfig.addressOptions.countryOptions.validCountries.isNotEmpty(),
                    routeConfig.requestOptions.mergePolicy,
                    invalidFields
                )
            )
        }
    }

    private fun isNetworkConnectionError(response: VGSResponse): Boolean =
        (response as? VGSResponse.ErrorResponse)?.code == VGSError.NO_NETWORK_CONNECTIONS.code

    private fun showNetworkConnectionErrorSnackBar() {
        val message = getString(R.string.vgs_checkout_no_network_error)
        Snackbar.make(findViewById(R.id.llRootView), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.vgs_checkout_no_network_retry)) { makeRequest() }
            .show()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun validateFields() {
        collect.validateFields()
    }
}