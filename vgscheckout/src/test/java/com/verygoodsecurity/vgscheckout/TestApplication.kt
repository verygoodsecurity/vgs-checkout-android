package com.verygoodsecurity.vgscheckout

import android.app.Application
import org.mockito.Mockito

internal class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.VGSCheckout)
    }
}

fun <T> any(): T = Mockito.any()
