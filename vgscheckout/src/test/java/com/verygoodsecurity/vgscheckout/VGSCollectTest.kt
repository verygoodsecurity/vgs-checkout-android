package com.verygoodsecurity.vgscheckout

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.verygoodsecurity.vgscheckout.collect.app.BaseTransmitActivity
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.api.DefaultApiClientStorage
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSHashMapWrapper
import com.verygoodsecurity.vgscheckout.collect.core.model.network.*
import com.verygoodsecurity.vgscheckout.collect.core.storage.InternalStorage
import com.verygoodsecurity.vgscheckout.collect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.file.TemporaryFileStorage
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.collect.widget.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class VGSCollectTest {
    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity

    private lateinit var collect: VGSCollect

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()
        collect = VGSCollect(activity, "tnts")
    }

    @Test
    fun test_add_response_listener() {
        applyResponseListener()
        applyResponseListener()

        assertEquals(3, collect.getResponseListeners().size) // + analytic listener
    }

    @Test
    fun test_remove_response_listener() {
        val listener1 = mock(VgsCollectResponseListener::class.java)
        collect.addOnResponseListeners(listener1)
        assertEquals(2, collect.getResponseListeners().size) // + analytic listener

        val listener2 = mock(VgsCollectResponseListener::class.java)
        collect.removeOnResponseListener(listener2)
        assertEquals(2, collect.getResponseListeners().size) // + analytic listener

        collect.removeOnResponseListener(listener1)
        assertEquals(1, collect.getResponseListeners().size) // + analytic listener
    }

    @Test
    fun test_remove_all_response_listeners() {
        val listener1 = mock(VgsCollectResponseListener::class.java)
        collect.addOnResponseListeners(listener1)
        assertEquals(2, collect.getResponseListeners().size) // + analytic listener
        val listener2 = mock(VgsCollectResponseListener::class.java)
        collect.addOnResponseListeners(listener2)
        assertEquals(3, collect.getResponseListeners().size) // + analytic listener

        collect.clearResponseListeners()
        assertEquals(1, collect.getResponseListeners().size) // + analytic listener
    }

    @Test
    fun test_add_state_change_listener() {
        val storage = applyStorage()

        applyStateChangeListener()
        applyStateChangeListener()

        verify(storage, times(2)).attachStateChangeListener(any())
    }

    @Test
    fun test_bind_view() {
        val view = applyEditText(FieldType.INFO)

        verify(view, times(1)).getFieldType()
        verify(view).getFieldName()
        verify(view).addStateListener(any())
    }

    @Test
    fun test_unbind_view() {
        val view = applyEditText(FieldType.INFO)
        assertEquals(1, collect.getAllStates().size)
        assertTrue(view.statePreparer.getView() is BaseInputField)
        assertNotNull((view.statePreparer.getView() as BaseInputField).stateListener)

        collect.unbindView(view)
        assertEquals(0, collect.getAllStates().size)
        assertNull((view.statePreparer.getView() as BaseInputField).stateListener)

        view.setText("SDS")
        assertEquals(0, collect.getAllStates().size)
        assertNull((view.statePreparer.getView() as BaseInputField).stateListener)
    }

    @Test
    fun test_on_destroy() {
        applyResponseListener()

        val client = applyApiClient()
        val storage = applyStorage()

        collect.onDestroy()

        assertEquals(0, collect.getResponseListeners().size)
        verify(storage).clear()

        verify(client, after(500)).cancelAll()
    }

    @Test
    fun test_get_all_states() {
        applyEditText(FieldType.CVC)
        applyEditText(FieldType.CARD_NUMBER)

        assertEquals(2, collect.getAllStates().size)
    }

    @Test
    fun test_get_all_states_4_fields() {
        applyEditText(FieldType.CVC)
        applyEditText(FieldType.CARD_NUMBER)
        applyEditText(FieldType.CARD_EXPIRATION_DATE)
        applyEditText(FieldType.CARD_HOLDER_NAME)

        assertEquals(4, collect.getAllStates().size)
    }

    @Test
    fun test_sync_path_method_no_internet_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val listener = applyResponseListener()

        collect.submit("/path", HTTPMethod.POST)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_sync_path_method_no_access_network_state_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET
        )

        val listener = applyResponseListener()

        collect.submit("/path", HTTPMethod.POST)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_sync_path_method() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val client = applyApiClient()

        doReturn(NetworkResponse())
            .`when`(client).execute(any())

        collect.submit("/path", HTTPMethod.POST)

        verify(client).execute(any())
    }

    @Test
    fun test_async_path_method_no_internet_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val listener = applyResponseListener()

        collect.asyncSubmit("/path", HTTPMethod.POST)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_async_path_method_no_access_network_state_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET
        )

        val listener = applyResponseListener()

        collect.asyncSubmit("/path", HTTPMethod.POST)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_async_path_method() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val client = applyApiClient()

        collect.asyncSubmit("/path", HTTPMethod.POST)

        verify(client, after(500)).enqueue(
            any(),
            any()
        )
    }

    @Test
    fun test_sync_request_builder_no_internet_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val listener = applyResponseListener()

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.submit(request)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_sync_request_builder_no_access_network_state_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET
        )

        val listener = applyResponseListener()

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.submit(request)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_sync_request_builder() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val client = applyApiClient()

        doReturn(NetworkResponse())
            .`when`(client).execute(any())

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.submit(request)

        verify(client).execute(any())
    }


    @Test
    fun test_async_request_builder_no_internet_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val listener = applyResponseListener()

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.asyncSubmit(request)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_async_request_builder_no_access_network_state_permission() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET
        )

        val listener = applyResponseListener()

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.asyncSubmit(request)

        verify(listener).onResponse(ArgumentMatchers.any(VGSResponse.ErrorResponse::class.java))
    }

    @Test
    fun test_async_request_builder() {
        val activityShadow = Shadows.shadowOf(activity)
        activityShadow.grantPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        val client = applyApiClient()

        val request = VGSRequest.VGSRequestBuilder()
            .setPath("/path")
            .setMethod(HTTPMethod.POST)
            .build()
        collect.asyncSubmit(request)

        verify(client, after(500)).enqueue(
            any(),
            any()
        )
    }

    @Test
    fun test_on_activity_result_pick_file() {
        val storage = applyStorage()

        val intent = Intent()
        val map = VGSHashMapWrapper<String, Any?>()
        intent.putExtra(BaseTransmitActivity.RESULT_DATA, map)

        collect.onActivityResult(TemporaryFileStorage.REQUEST_CODE, Activity.RESULT_OK, intent)

        verify(storage).getFileStorage()
    }

    @Test
    fun test_set_custom_headers() {
        val client = applyApiClient()

        val data = HashMap<String, String>()
        data["key"] = "value"
        collect.setCustomHeaders(data)

        verify(client).getStorage()
        assertEquals(1, client.getStorage().getCustomHeaders().size)
    }

    @Test
    fun test_reset_custom_headers() {
        val client = applyApiClient()

        collect.resetCustomHeaders()

        verify(client).getStorage()
        assertEquals(0, client.getStorage().getCustomHeaders().size)
    }

    @Test
    fun test_set_custom_data() {
        val client = applyApiClient()

        val data = HashMap<String, String>()
        data["key"] = "value"
        collect.setCustomData(data)

        verify(client).getStorage()
        assertEquals(1, client.getStorage().getCustomData().size)
    }

    @Test
    fun testResetCustomData() {
        val client = applyApiClient()

        collect.resetCustomData()

        verify(client).getStorage()
        assertEquals(0, client.getStorage().getCustomData().size)
    }

    @Test
    fun test_get_file_provider() {
        val storage = applyStorage()
        collect.getFileProvider()
        verify(storage).getFileProvider()
    }

    private fun applyStorage(): InternalStorage {
        val storage = spy( InternalStorage(activity) )
        collect.setStorage(storage)

        return storage
    }

    private fun applyEditText(typeField: FieldType):InputFieldView {
        val view = when(typeField) {
            FieldType.CARD_NUMBER -> createCardNumber()
            FieldType.CVC -> createCardCVC()
            FieldType.CARD_EXPIRATION_DATE -> createCardExpDate()
            FieldType.CARD_HOLDER_NAME -> createCardHolder()
            else -> spy( VGSEditText(activity) ).apply {
                setFieldName("createInfoField")
            }
        }

        (view.statePreparer.getView() as? BaseInputField)?.prepareFieldTypeConnection()

        collect.bindView(view)


        return view
    }

    private fun createCardNumber():VGSCardNumberEditText {
        return  spy( VGSCardNumberEditText(activity) ).apply {
            setFieldName("createCardNumber")
        }
    }

    private fun createCardCVC():CardVerificationCodeEditText {
        return  spy( CardVerificationCodeEditText(activity) ).apply {
            setFieldName("createCardCVC")
        }
    }

    private fun createCardHolder():PersonNameEditText {
        return  spy( PersonNameEditText(activity) ).apply {
            setFieldName("createCardHolder")
        }
    }

    private fun createCardExpDate():ExpirationDateEditText {
        return  spy( ExpirationDateEditText(activity) ).apply {
            setFieldName("createCardExpDate")
        }
    }

    private fun applyResponseListener(): VgsCollectResponseListener {
        val listener = mock(VgsCollectResponseListener::class.java)
        collect.addOnResponseListeners(listener)

        return listener
    }

    private fun applyStateChangeListener(): OnFieldStateChangeListener {
        val listener = mock(OnFieldStateChangeListener::class.java)
        collect.addOnFieldStateChangeListener(listener)

        return listener
    }

    private fun applyApiClient(): ApiClient {
        val client = mock(ApiClient::class.java)
        doReturn(DefaultApiClientStorage())
            .`when`(client).getStorage()

        collect.setClient(client)

        return client
    }
}