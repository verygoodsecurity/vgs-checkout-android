package com.verygoodsecurity.vgscheckout.collect.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import androidx.annotation.VisibleForTesting
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.VGSCollectLogger
import com.verygoodsecurity.vgscheckout.collect.app.BaseTransmitActivity
import com.verygoodsecurity.vgscheckout.collect.core.api.*
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.*
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.utils.toAnalyticStatus
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.api.client.extension.isHttpStatusCode
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSCollectFieldNameMappingPolicy.*
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSHashMapWrapper
import com.verygoodsecurity.vgscheckout.collect.core.model.network.*
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.core.model.state.mapToFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.*
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.file.StorageErrorListener
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.file.TemporaryFileStorage
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.file.VGSFileProvider
import com.verygoodsecurity.vgscheckout.collect.core.storage.external.DependencyReceiver
import com.verygoodsecurity.vgscheckout.collect.core.storage.external.ExternalDependencyDispatcher
import com.verygoodsecurity.vgscheckout.collect.util.*
import com.verygoodsecurity.vgscheckout.collect.util.extension.*
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.VGSCollectView
import java.util.*

/**
 * VGS Collect allows you to securely collect data and files from your users without having
 * to have them pass through your systems.
 * Entry-point to the Collect SDK.
 *
 * @since 1.0.0
 */
internal class VGSCollect {

    private val externalDependencyDispatcher: ExternalDependencyDispatcher

    private val tracker: AnalyticTracker?

    private var client: ApiClient
    private val mainHandler: Handler = Handler(Looper.getMainLooper())

    private var storage: InternalStorage
    private val storageErrorListener: StorageErrorListener = object : StorageErrorListener {
        override fun onStorageError(error: VGSError) {
            error.toVGSResponse(context).also { r ->
                notifyAllListeners(r)
                VGSCollectLogger.warn(InputFieldView.TAG, r.message)
            }
        }
    }

    private val responseListeners = mutableListOf<VgsCollectResponseListener>()
    private val analyticListener = object : VgsCollectResponseListener {
        override fun onResponse(response: VGSResponse?) {
            response?.let {
                responseEvent(it.code, it.latency, (it as? VGSResponse.ErrorResponse)?.message)
            }
        }
    }

    private val baseURL: String
    private val context: Context

    private var cname: String? = null

    private constructor(
        context: Context,
        id: String,
        environment: String,
        url: String?,
        port: Int?,
        tracker: AnalyticTracker?
    ) {
        this.context = context
        this.storage = InternalStorage(context, storageErrorListener)
        this.externalDependencyDispatcher = DependencyReceiver()
        this.client = ApiClient.create()
        this.baseURL = generateBaseUrl(id, environment, url, port)
        this.tracker = tracker
        cname?.let { configureHostname(it, id) }
        updateAgentHeader()
        addOnResponseListeners(analyticListener)
    }

    constructor(
        /** Activity context */
        context: Context,

        /** Unique Vault id */
        id: String,

        /** Type of Vault */
        environment: String
    ) : this(context, id, environment, null, null, null)

    constructor(
        /** Activity context */
        context: Context,

        /** Unique Vault id */
        id: String,

        /** Type of Vault */
        environment: Environment = Environment.SANDBOX
    ) : this(context, id, environment.rawValue, null, null, null)

    constructor(
        /** Activity context */
        context: Context,

        /** Unique Vault id */
        id: String,

        /** Type of Environment */
        environmentType: String,

        /** Region identifier */
        suffix: String
    ) : this(context, id, environmentType concatWithDash suffix, null, null, null)

    /**
     * Adds a listener to the list of those whose methods are called whenever the VGSCollect receive response from Server.
     *
     * @param onResponseListener Interface definition for a receiving callback.
     */
    fun addOnResponseListeners(onResponseListener: VgsCollectResponseListener?) {
        onResponseListener?.let {
            if (!responseListeners.contains(it)) responseListeners.add(it)
        }
    }

    /**
     * Clear all response listeners attached before.
     */
    fun clearResponseListeners() {
        responseListeners.clear()
        responseListeners.add(analyticListener)
    }

    /**
     * Clear specific listener attached before.
     *
     * @param onResponseListener Interface definition for a receiving callback.
     */
    fun removeOnResponseListener(onResponseListener: VgsCollectResponseListener) {
        if (responseListeners.contains(onResponseListener)) responseListeners.remove(
            onResponseListener
        )
    }

    /**
     * Allows VGS secure fields to interact with [VGSCollect] and collect data from this source.
     *
     * @param view base class for VGS secure fields.
     */
    fun bindView(view: VGSCollectView?) {
        view?.statePreparer?.let {
            externalDependencyDispatcher.addDependencyListener(
                view.getFieldName(),
                it.getDependencyListener()
            )

            it.setAnalyticTracker(tracker)
        }

        storage.performSubscription(view)
    }

    /**
     * Allows VGS secure fields to interact with [VGSCollect] and collect data from this source.
     *
     * @param views VGS secure views.
     */
    fun bindView(vararg views: VGSCollectView?) {
        views.forEach {
            bindView(it)
        }
    }

    /**
     * Allows to unsubscribe from a View updates.
     *
     * @param view base class for VGS secure fields.
     */
    fun unbindView(view: VGSCollectView?) {
        view?.let {
            storage.unsubscribe(view)
        }
    }

    /**
     * Allows to unsubscribe from a View updates.
     *
     * @param views VGS secure views.
     */
    fun unbindView(vararg views: VGSCollectView?) {
        views.forEach {
            unbindView(it)
        }
    }

    /**
     * This method adds a listener whose methods are called whenever VGS secure fields state changes.
     *
     * @param fieldStateListener listener which will notify about changes inside input fields.
     */
    fun addOnFieldStateChangeListener(fieldStateListener: OnFieldStateChangeListener?) {
        storage.attachStateChangeListener(fieldStateListener)
    }

    /**
     * Clear all information collected before by VGSCollect.
     * Preferably call it inside onDestroy system's callback.
     */
    fun onDestroy() {
        client.cancelAll()
        responseListeners.clear()
        storage.clear()
    }

    /**
     * Returns the states of all fields bound before to VGSCollect.
     *
     * @return the list of all input fields states, that were bound before.
     */
    fun getAllStates(): List<FieldState> {
        return storage.getFieldsStates().map { it.mapToFieldState() }
    }

    /**
     * This method executes and send data on VGS Server. It could be useful if you want to handle
     * multithreading by yourself.
     * Do not use this method on the UI thread as this may crash.
     *
     * @param path path for a request
     * @param method HTTP method
     */
    fun submit(path: String, method: HTTPMethod = HTTPMethod.POST): VGSResponse {
        val request = VGSRequest.VGSRequestBuilder()
            .setPath(path)
            .setMethod(method)
            .build()

        return submit(request)
    }

    /**
     * This method executes and send data on VGS Server. It could be useful if you want to handle
     * multithreading by yourself.
     * Do not use this method on the UI thread as this may crash.
     *
     * @param request data class with attributes for submit.
     */
    fun submit(request: VGSRequest): VGSResponse {
        var response: VGSResponse = VGSResponse.ErrorResponse()

        collectUserData(request) {
            response = client.execute(
                request.toNetworkRequest(baseURL, it)
            ).toVGSResponse(context)
        }

        return response
    }

    /**
     * This method executes and send data on VGS Server.
     *
     * @param path path for a request
     * @param method HTTP method
     */
    fun asyncSubmit(
        path: String, method: HTTPMethod
    ) {
        val request = VGSRequest.VGSRequestBuilder()
            .setPath(path)
            .setMethod(method)
            .build()

        asyncSubmit(request)
    }

    /**
     * This method executes and send data on VGS Server.
     *
     * @param request data class with attributes for submit
     */
    fun asyncSubmit(request: VGSRequest) {
        collectUserData(request) {
            client.enqueue(request.toNetworkRequest(baseURL, it)) { r ->
                mainHandler.post { notifyAllListeners(r.toVGSResponse()) }
            }
        }
    }

    private fun collectUserData(request: VGSRequest, submitRequest: (Map<String, Any>) -> Unit) {
        when {
            !request.fieldsIgnore && !validateFields() -> return
            !request.fileIgnore && !validateFiles() -> return
            !baseURL.isURLValid() -> notifyAllListeners(VGSError.URL_NOT_VALID.toVGSResponse(context))
            !context.hasInternetPermission() ->
                notifyAllListeners(VGSError.NO_INTERNET_PERMISSIONS.toVGSResponse(context))
            !context.hasAccessNetworkStatePermission() ->
                notifyAllListeners(VGSError.NO_NETWORK_CONNECTIONS.toVGSResponse(context))
            !context.isConnectionAvailable() ->
                notifyAllListeners(VGSError.NO_NETWORK_CONNECTIONS.toVGSResponse(context))
            else -> submitRequest(mergeData(request))
        }
    }

    private fun notifyAllListeners(r: VGSResponse) {
        responseListeners.forEach { it.onResponse(r) }
    }

    private fun validateFiles(): Boolean {
        var isValid = true

        storage.getAttachedFiles().forEach {
            if (it.size > storage.getFileSizeLimit()) {
                notifyAllListeners(VGSError.FILE_SIZE_OVER_LIMIT.toVGSResponse(context, it.name))

                isValid = false
                return@forEach
            }
        }

        return isValid
    }

    private fun validateFields(): Boolean {
        var isValid = true

        storage.getFieldsStorage().getItems().forEach {
            if (it.isValid.not()) {
                VGSError.INPUT_DATA_NOT_VALID.toVGSResponse(context, it.fieldName).also { r ->
                    notifyAllListeners(r)
                    VGSCollectLogger.warn(InputFieldView.TAG, r.message)
                }

                isValid = false
                return isValid
            }
        }
        return isValid
    }

    private fun mergeData(request: VGSRequest): Map<String, Any> {
        val (allowArrays, mergeArraysPolicy) = when (request.fieldNameMappingPolicy) {
            FLAT_JSON -> null to ArrayMergePolicy.OVERWRITE
            NESTED_JSON -> false to ArrayMergePolicy.OVERWRITE
            NESTED_JSON_WITH_ARRAYS_MERGE -> true to ArrayMergePolicy.MERGE
            NESTED_JSON_WITH_ARRAYS_OVERWRITE -> true to ArrayMergePolicy.OVERWRITE
        }

        return with(client.getStorage().getCustomData().toMutableMap()) { // Static additional data
            // Merge dynamic additional data
            deepMerge(request.customData, mergeArraysPolicy)

            val fieldsData = allowArrays?.let {
                storage.getAssociatedList(request.fieldsIgnore, request.fileIgnore)
                    .toFlatMap(it).structuredData
            } ?: storage.getAssociatedList(request.fieldsIgnore, request.fileIgnore).toMap()

            deepMerge(fieldsData, mergeArraysPolicy)
        }
    }

    /**
     * Called when an activity you launched exits,
     * giving you the requestCode you started it with, the resultCode is returned,
     * and any additional data for VGSCollect.
     * Preferably call it inside onActivityResult system's callback.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mapAnalyticEvent(data)

        if (resultCode == Activity.RESULT_OK) {
            val map: VGSHashMapWrapper<String, Any?>? = data?.extras?.getParcelable(
                BaseTransmitActivity.RESULT_DATA
            )

            if (requestCode == TemporaryFileStorage.REQUEST_CODE) {
                map?.run {
                    storage.getFileStorage().dispatch(mapOf())
                }
            } else {
                map?.run {
                    externalDependencyDispatcher.dispatch(mapOf())
                }
            }
        }
    }

    private fun mapAnalyticEvent(data: Intent?) {
        data?.let {
            val map: VGSHashMapWrapper<String, Any?> = it.extras?.getParcelable(
                BaseTransmitActivity.RESULT_DATA
            ) ?: VGSHashMapWrapper()

            if (map.get(BaseTransmitActivity.RESULT_TYPE) == BaseTransmitActivity.SCAN) {
                tracker?.log(
                    Scan(
                        map.get(BaseTransmitActivity.RESULT_STATUS).toString(),
                        map.get(BaseTransmitActivity.RESULT_NAME).toString(),
                        map.get(BaseTransmitActivity.RESULT_ID) as? String
                    )
                )
            }
        }
    }

    /**
     * It collects headers that will be sent to the server.
     * This is static headers that are stored and attach for all requests until @resetCustomHeaders method will be called.
     *
     * @param headers The headers to save for request.
     */
    fun setCustomHeaders(headers: Map<String, String>) {
        client.getStorage().setCustomHeaders(headers)
    }

    /**
     * Reset all static headers which added before.
     * This method has no impact on all custom data that were added with [VGSRequest]
     */
    fun resetCustomHeaders() {
        client.getStorage().resetCustomHeaders()
    }

    /**
     * It collect custom data which will be send to server.
     * This is static custom data that are stored and attach for all requests until resetCustomData method will be called.
     *
     * @param data The Map to save for request.
     */
    fun setCustomData(data: Map<String, Any>) {
        client.getStorage().setCustomData(data)
    }

    /**
     * Reset all static custom data which added before.
     * This method has no impact on all custom data that were added with [VGSRequest]
     */
    fun resetCustomData() {
        client.getStorage().resetCustomData()
    }

    /**
     * Return instance for managing attached files to request.
     *
     * @return [VGSFileProvider] instance
     */
    fun getFileProvider(): VGSFileProvider {
        return storage.getFileProvider()
    }

    /**
     * If you want to disable collecting analytics from VGS Collect SDK, you can set the value to false.
     * This helps us to understand which areas require improvements.
     * No personal information is tracked.
     *
     * Warning: if this option is set to false, it will increase resolving time for possible incidents.
     */
    fun setAnalyticsEnabled(isEnabled: Boolean) {
        tracker?.isEnabled = isEnabled
        updateAgentHeader(isEnabled)
    }

    @VisibleForTesting
    internal fun getResponseListeners(): Collection<VgsCollectResponseListener> {
        return responseListeners
    }

    @VisibleForTesting
    internal fun setStorage(store: InternalStorage) {
        storage = store
    }

    @VisibleForTesting
    internal fun setClient(c: ApiClient) {
        client = c
    }

    private fun responseEvent(code: Int, latency: Long, message: String? = null) {
        if (code.isHttpStatusCode()) {
            tracker?.log(Response(code, latency, message))
        }
    }

    private var hasCustomHostname = false

    private fun generateBaseUrl(id: String, environment: String, url: String?, port: Int?): String {

        fun printPortDenied() {
            if (port.isValidPort()) {
                VGSCollectLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_port_is_not_allowed))
            }
        }

        if (!url.isNullOrBlank() && url.isURLValid()) {
            val host = getHost(url)
            if (host.isValidIp()) {
                if (!host.isIpAllowed()) {
                    VGSCollectLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_ip_is_not_allowed))
                    return id.setupURL(environment)
                }
                if (!environment.isSandbox()) {
                    VGSCollectLogger.warn(message = context.getString(R.string.vgs_checkout_error_env_incorrect))
                    return id.setupURL(environment)
                }
                return host.setupLocalhostURL(port)
            } else {
                printPortDenied()
                cname = host
                return id.setupURL(environment)
            }
        } else {
            printPortDenied()
            return id.setupURL(environment)
        }
    }

    private fun getHost(url: String) = url.toHost().also {
        if (it != url) {
            VGSCollectLogger.debug(message = "Hostname will be normalized to the $it")
        }
    }

    private fun configureHostname(host: String, tnt: String) {
        if (host.isNotBlank() && baseURL.isNotEmpty()) {
            val r = VGSRequest.VGSRequestBuilder()
                .setMethod(HTTPMethod.GET)
                .setFormat(VGSHttpBodyFormat.PLAIN_TEXT)
                .build()
                .toNetworkRequest(host.toHostnameValidationUrl(tnt))

            client.enqueue(r) {
                hasCustomHostname = it.isSuccessful && host equalsUrl it.body
                if (hasCustomHostname) {
                    client.setHost(it.body)
                } else {
                    context.run {
                        VGSCollectLogger.warn(
                            message = String.format(
                                getString(R.string.vgs_checkout_error_custom_host_wrong),
                                host
                            )
                        )
                    }
                }
                tracker?.log(HostnameValidation(hasCustomHostname.toAnalyticStatus(), host))
            }
        }
    }

    private fun updateAgentHeader(isAnalyticsEnabled: Boolean = true) {
        client.getStorage().setCustomHeaders(
            mapOf(
                AGENT to String.format(
                    AGENT_FORMAT,
                    BuildConfig.VERSION_NAME,
                    if (isAnalyticsEnabled) AGENT_ANALYTICS_ENABLED else AGENT_ANALYTICS_DISABLED
                )
            )
        )
    }

    companion object {

        private const val AGENT = "VGS-Client"
        private const val AGENT_FORMAT = "source=vgs-checkout&medium=vgs-checkout&content=%s&tr=%s"
        private const val AGENT_ANALYTICS_ENABLED = "default"
        private const val AGENT_ANALYTICS_DISABLED = "none"
    }

    /**
     * Used to create VGSCollect instances with default and overridden settings.
     *
     * @constructor Main constrictor for creating VGSCollect instance builder.
     * @param context Activity context.
     * @param id Specific Vault ID.
     */
    class Builder(private val context: Context, private val id: String) {

        private var environment: String = Environment.SANDBOX.rawValue
        private var host: String? = null
        private var port: Int? = null
        private var analyticsTracker: AnalyticTracker? = null

        /** Specify Environment for the VGSCollect instance. */
        fun setEnvironment(env: Environment, region: String = ""): Builder = this.apply {
            environment = env.rawValue concatWithDash region
        }

        /** Specify Environment for the VGSCollect instance. */
        fun setEnvironment(env: Environment): Builder = this.apply {
            environment = env.rawValue
        }

        /**
         * Specify Environment for the VGSCollect instance.
         * Also, Environment could be used with region prefix ( sandbox-eu-0 ).
         */
        fun setEnvironment(env: String): Builder = this.apply { environment = env }

        /**
         * Sets the VGSCollect instance to use the custom hostname.
         * Also, the localhost IP can be used for VGS-Satellite for local testing.
         *
         * @param cname where VGSCollect will send requests.
         */
        fun setHostname(cname: String): Builder = this.apply {
            if (!cname.isURLValid()) {
                VGSCollectLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_host_wrong_short))
                return@apply
            }
            this.host = cname
        }

        /**
         * Sets the VGSCollect instance to use the custom hostname port.
         * Port can be used only with localhost with VGS-Satellite, otherwise, it will be ignored.
         *
         * @param port Integer value from 1 to 65353.
         */
        fun setPort(
            @IntRange(from = PORT_MIN_VALUE, to = PORT_MAX_VALUE) port: Int
        ) = this.apply { this.port = port }

        /**
         * Sets the VGSCollect analytics tracker.
         *
         * @param tracker Implementation of AnalyticTracker.
         */
        fun setAnalyticTracker(tracker: AnalyticTracker?) = this.apply {
            this.analyticsTracker = tracker
        }

        /**
         * Creates an VGSCollect with the arguments supplied to this
         * builder.
         */
        fun create() = VGSCollect(context, id, environment, host, port, analyticsTracker)
    }
}