package com.verygoodsecurity.vgscheckout.ui.fragment.core

import android.content.Intent

internal interface ActivityResultHandler {

    fun onResult(requestCode: Int, resultCode: Int, data: Intent?)
}